package dev.rao.rockmarket.auth.login.domain.usecase

import dev.rao.rockmarket.auth.login.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.signOut()
    }
} 