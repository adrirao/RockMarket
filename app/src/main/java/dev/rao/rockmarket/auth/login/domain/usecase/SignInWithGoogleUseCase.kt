package dev.rao.rockmarket.auth.login.domain.usecase

import com.google.firebase.auth.AuthCredential
import dev.rao.rockmarket.auth.login.domain.model.User
import dev.rao.rockmarket.auth.login.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(credential: AuthCredential): Result<User> {
        return authRepository.signInWithGoogle(credential)
    }
} 