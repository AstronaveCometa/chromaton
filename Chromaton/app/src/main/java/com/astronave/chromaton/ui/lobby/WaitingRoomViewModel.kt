package com.astronave.chromaton.ui.lobby

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astronave.chromaton.data.network.Player
import com.astronave.chromaton.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WaitingRoomViewModel : ViewModel() {

    private val _players = MutableLiveData<List<Player>>()
    val players: LiveData<List<Player>> get() = _players

    // --- LA PIEZA QUE FALTABA ---
    private val _gameStatus = MutableLiveData<String>()
    val gameStatus: LiveData<String> get() = _gameStatus

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun startPolling(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    // 1. Consultar Jugadores
                    val playersResponse = RetrofitClient.instance.getPlayersByGameId(gameId)
                    if (playersResponse.isSuccessful) {
                        val lista = playersResponse.body() ?: emptyList()
                        _players.postValue(lista)
                        Log.d("POLLING_DEBUG", "Jugadores: ${lista.size}")
                    }

                    // 2. Consultar Estado del Juego
                    val gameResponse = RetrofitClient.instance.getGameById(gameId)
                    if (gameResponse.isSuccessful) {
                        val game = gameResponse.body()
                        val status = game?.status ?: "waiting"

                        Log.d("POLLING_DEBUG", "Estado actual: $status")

                        if (status == "playing") {
                            _gameStatus.postValue("playing")
                            Log.d("POLLING_DEBUG", "¡Partida iniciada! Deteniendo polling...")
                            return@launch // Cerramos el bucle
                        }
                    }

                } catch (e: Exception) {
                    val faultMsg = "Error de red: ${e.localizedMessage}"
                    _error.postValue(faultMsg)
                    Log.e("POLLING_DEBUG", faultMsg)
                }

                delay(3000) // Espera 3 segundos antes de repetir
            }
        }
    }

    // Función para que el Host inicie la partida
    fun startGame(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.startGame(gameId)
                if (!response.isSuccessful) {
                    _error.postValue("No se pudo iniciar la partida")
                }
            } catch (e: Exception) {
                _error.postValue("Error al conectar: ${e.message}")
            }
        }
    }
}