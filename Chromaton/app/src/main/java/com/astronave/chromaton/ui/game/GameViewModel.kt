package com.astronave.chromaton.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astronave.chromaton.data.network.GameDetailResponse
import com.astronave.chromaton.data.network.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GameViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _gameState = MutableLiveData<GameDetailResponse>()
    val gameState: LiveData<GameDetailResponse> get() = _gameState

    fun startPollingGame(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val tokenResult = auth.currentUser?.getIdToken(true)?.await()
                    val token = "Bearer ${tokenResult?.token}"

                    val response = RetrofitClient.instance.getGameDetails(token, gameId)
                    if (response.isSuccessful) {
                        _gameState.postValue(response.body())
                    }
                } catch (e: Exception) {
                    Log.e("GAME_DEBUG", "Error: ${e.message}")
                }
                delay(2000)
            }
        }
    }
}