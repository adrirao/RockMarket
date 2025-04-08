package dev.rao.rockmarket.auth.login.domain.usecase

import dev.rao.rockmarket.auth.login.domain.repository.AuthRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return authRepository.isUserLoggedIn()
    }
} 