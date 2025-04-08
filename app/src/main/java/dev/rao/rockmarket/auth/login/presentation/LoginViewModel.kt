package dev.rao.rockmarket.auth.login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rao.rockmarket.auth.login.domain.usecase.IsUserLoggedInUseCase
import dev.rao.rockmarket.auth.login.domain.usecase.SignInWithEmailUseCase
import dev.rao.rockmarket.auth.login.domain.usecase.SignInWithGoogleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
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
                    _state.value = LoginState.Error(exception.message ?: "Error desconocido")
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
                    _state.value = LoginState.Error(exception.message ?: "Error desconocido")
                }
        }
    }
}