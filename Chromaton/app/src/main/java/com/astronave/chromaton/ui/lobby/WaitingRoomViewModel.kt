package com.astronave.chromaton.ui.lobby

import android.util.Log
import androidx.lifecycle.*
import com.astronave.chromaton.data.network.Player
import com.astronave.chromaton.data.network.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class WaitingRoomViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _players = MutableLiveData<List<Player>>()
    val players: LiveData<List<Player>> get() = _players

    private val _gameStatus = MutableLiveData<String>()
    val gameStatus: LiveData<String> get() = _gameStatus

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun startPolling(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    // Obtenemos el token fresco en cada ciclo (por si expira)
                    val tokenResult = auth.currentUser?.getIdToken(true)?.await()
                    val token = "Bearer ${tokenResult?.token}"

                    // 1. Consultar Jugadores
                    val playersResponse = RetrofitClient.instance.getPlayersByGameId(gameId)
                    if (playersResponse.isSuccessful) {
                        _players.postValue(playersResponse.body() ?: emptyList())
                    }

                    // 2. Consultar Estado del Juego (Ahora con Token)
                    val gameResponse = RetrofitClient.instance.getGameById(token, gameId)
                    if (gameResponse.isSuccessful) {
                        val status = gameResponse.body()?.status ?: "waiting"
                        if (status == "playing") {
                            _gameStatus.postValue("playing")
                            return@launch
                        }
                    }
                } catch (e: Exception) {
                    Log.e("POLLING_DEBUG", "Error: ${e.localizedMessage}")
                }
                delay(3000)
            }
        }
    }

    fun startGame(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Obtenemos el token para autorizar la acción del Host
                val tokenResult = auth.currentUser?.getIdToken(true)?.await()
                val token = "Bearer ${tokenResult?.token}"

                val response = RetrofitClient.instance.startGame(token, gameId)

                if (!response.isSuccessful) {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    _error.postValue("No se pudo iniciar: $errorMsg")
                }
            } catch (e: Exception) {
                _error.postValue("Error al conectar: ${e.message}")
            }
        }
    }
}