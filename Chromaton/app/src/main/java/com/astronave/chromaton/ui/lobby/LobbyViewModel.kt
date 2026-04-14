package com.astronave.chromaton.ui.lobby

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

import com.astronave.chromaton.data.network.RetrofitClient
import com.astronave.chromaton.data.network.CreateGameRequest
import com.astronave.chromaton.data.network.JoinGameRequest

class LobbyViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Estado para saber si el usuario cerró sesión con éxito
    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> get() = _navigateToLogin

    // Datos básicos del usuario actual de Firebase
    private val _userEmail = MutableLiveData<String?>()
    val userEmail: LiveData<String?> get() = _userEmail

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _navigateToLogin.value = true
        } else {
            _userEmail.value = currentUser.email
        }
    }

    fun logout() {
        auth.signOut()
        _navigateToLogin.value = true
    }

    // Función que usaremos más adelante para disparar la carga desde Express
    fun getFirebaseUid(): String? {
        return auth.currentUser?.uid
    }

    private val _gameCreated = MutableLiveData<Int?>()
    val gameCreated: LiveData<Int?> get() = _gameCreated

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun createPrivateGame(password: String) {
        viewModelScope.launch {
            try {
                val tokenResult = auth.currentUser?.getIdToken(true)?.await()
                val token = "Bearer ${tokenResult?.token}"

                val response = RetrofitClient.instance.createGame(token, CreateGameRequest(password))

                if (response.isSuccessful) {
                    Log.d("API_RES", "Body: ${response.body()}")
                    _gameCreated.value = response.body()?.game_id
                    _error.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error: $errorBody"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
            }
        }
    }

    private val _gameJoined = MutableLiveData<Int?>()
    val gameJoined: LiveData<Int?> get() = _gameJoined

    fun joinPrivateGame(gameId: Int, password: String) {
        viewModelScope.launch {
            try {
                val tokenResult = auth.currentUser?.getIdToken(true)?.await()
                val token = "Bearer ${tokenResult?.token}"

                val response = RetrofitClient.instance.joinGame(
                    token,
                    JoinGameRequest(gameId, password)
                )

                if (response.isSuccessful) {
                    Log.d("API_RES", "Body: ${response.body()}")
                    _gameJoined.value = response.body()?.game_id
                    _error.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error: $errorBody"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
            }
        }
    }
}