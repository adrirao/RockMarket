package dev.rao.rockmarket.auth.login.domain.usecase

import dev.rao.rockmarket.auth.login.domain.model.User
import dev.rao.rockmarket.auth.login.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email y contraseña no pueden estar vacíos"))
        }

        return authRepository.signInWithEmailAndPassword(email, password)
    }
} 