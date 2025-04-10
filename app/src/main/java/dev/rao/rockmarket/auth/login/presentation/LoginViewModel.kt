package dev.rao.rockmarket.auth.login.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.rao.rockmarket.auth.login.domain.usecase.IsUserLoggedInUseCase
import dev.rao.rockmarket.auth.login.domain.usecase.SignInWithEmailUseCase
import dev.rao.rockmarket.auth.login.domain.usecase.SignInWithGoogleUseCase
import dev.rao.rockmarket.core.util.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    isUserLoggedInUseCase: IsUserLoggedInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Initial)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    val isUserLoggedIn = isUserLoggedInUseCase().apply {
        Log.d("LoginViewModel", "isUserLoggedIn: $this")
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            signInWithEmailUseCase(email, password)
                .onSuccess {
                    _state.value = LoginState.Success
                }
                .onFailure { exception ->
                    val message = if (NetworkUtils.isNetworkConnected(context)) {
                        exception.message ?: "Error desconocido"
                    } else {
                        "No hay conexión a internet"
                    }
                    _state.value = LoginState.Error(message)
                }
        }
    }

    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            signInWithGoogleUseCase(credential)
                .onSuccess {
                    _state.value = LoginState.Success
                }
                .onFailure { exception ->
                    Log.e("LoginViewModel", "Error al iniciar sesión con Google", exception)
                    val message = if (NetworkUtils.isNetworkConnected(context)) {
                        "Error al iniciar sesión con Google. Por favor, inténtalo más tarde."
                    } else {
                        "No hay conexión a internet"
                    }
                    _state.value = LoginState.Error(message)
                }
        }
    }
}