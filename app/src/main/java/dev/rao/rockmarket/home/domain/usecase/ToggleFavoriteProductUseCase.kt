package dev.rao.rockmarket.home.domain.usecase

import dev.rao.rockmarket.core.domain.model.Product
import dev.rao.rockmarket.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavoriteProductUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(product: Product, countryId: String) {
        val isFavorite = favoriteRepository.isFavorite(product.id).first()

        if (isFavorite) {
            favoriteRepository.removeFromFavorites(product.id)
        } else {
            favoriteRepository.addToFavorites(product, countryId)
        }
    }
} 