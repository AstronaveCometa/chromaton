package com.astronave.chromaton.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.lifecycle.viewModelScope
import com.astronave.chromaton.data.network.RetrofitClient
import com.astronave.chromaton.data.network.UserRequest
import kotlinx.coroutines.launch
import com.google.android.gms.tasks.Tasks

class AuthViewModel : ViewModel() {

    // 1. Instancia de Firebase Auth
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // 2. Estados de la Interfaz (Livedata)
    // El guion bajo "_" indica que es privado (solo el ViewModel lo modifica)
    private val _userState = MutableLiveData<FirebaseUser?>()
    val userState: LiveData<FirebaseUser?> get() = _userState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // 3. Función para Registrarse
// ... dentro de AuthViewModel
    fun register(email: String, pass: String, name: String) {
        _isLoading.value = true
        _errorMessage.value = null

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registro en Firebase exitoso, ahora vamos a PostgreSQL
                    val user = auth.currentUser
                    user?.getIdToken(true)?.addOnSuccessListener { result ->
                        val token = "Bearer ${result.token}"
                        // Llamamos a la función que habla con Render
                        saveUserInBackend(token, name, email)
                    }
                } else {
                    _isLoading.value = false
                    _errorMessage.value = task.exception?.localizedMessage ?: "Error en Firebase"
                }
            }
    }

    private fun saveUserInBackend(token: String, name: String, email: String) {
        // Usamos viewModelScope.launch para ejecutar la petición en un hilo secundario
        viewModelScope.launch {
            try {
                val request = UserRequest(name, email)
                val response = RetrofitClient.instance.registerUser(token, request)

                if (response.isSuccessful) {
                    // ¡Éxito total! El usuario está en Firebase y en PostgreSQL
                    _userState.postValue(auth.currentUser)
                    _isUserLoggedIn.postValue(true)
                } else {
                    _errorMessage.postValue("Error en el servidor: ${response.code()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de conexión: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // 4. Función para Iniciar Sesión
    fun login(email: String, pass: String) {
        _isLoading.value = true
        _errorMessage.value = null

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _userState.value = auth.currentUser
                    _isUserLoggedIn.value = true
                } else {
                    _errorMessage.value = task.exception?.localizedMessage ?: "Credenciales incorrectas"
                }
            }
    }

    // 5. Cerrar Sesión
    fun logout() {
        auth.signOut()
        _userState.value = null
    }

    // LiveData para observar si el usuario ya estaba logueado
    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    fun checkActiveSession() {
        val user = auth.currentUser
        if (user != null) {
            _isUserLoggedIn.value = true
        }
    }
}