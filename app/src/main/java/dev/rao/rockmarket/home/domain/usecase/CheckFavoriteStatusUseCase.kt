package dev.rao.rockmarket.home.domain.usecase

import dev.rao.rockmarket.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckFavoriteStatusUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(productId: String): Flow<Boolean> {
        return favoriteRepository.isFavorite(productId)
    }
} 