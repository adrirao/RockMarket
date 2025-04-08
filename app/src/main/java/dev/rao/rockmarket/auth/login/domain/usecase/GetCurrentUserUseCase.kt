package dev.rao.rockmarket.auth.login.domain.usecase

import dev.rao.rockmarket.auth.login.domain.model.User
import dev.rao.rockmarket.auth.login.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> {
        return authRepository.getCurrentUser()
    }
} 