package dev.rao.rockmarket.core.domain.repository

import dev.rao.rockmarket.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addToFavorites(product: Product, countryId: String)
    suspend fun removeFromFavorites(productId: String)
    fun getFavoritesByCountry(countryId: String): Flow<List<Product>>
    fun getAllFavorites(): Flow<List<Product>>
    fun isFavorite(productId: String): Flow<Boolean>
} 