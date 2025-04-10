package dev.rao.rockmarket.core.data.repository

import dev.rao.rockmarket.core.data.local.dao.FavoriteProductDao
import dev.rao.rockmarket.core.data.local.entity.FavoriteProductEntity
import dev.rao.rockmarket.core.domain.model.Product
import dev.rao.rockmarket.core.domain.model.Rating
import dev.rao.rockmarket.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteProductDao: FavoriteProductDao
) : FavoriteRepository {

    override suspend fun addToFavorites(product: Product, countryId: String) {
        val entity = product.toEntity(countryId)
        favoriteProductDao.insertFavorite(entity)
    }

    override suspend fun removeFromFavorites(productId: String) {
        favoriteProductDao.removeFavorite(productId)
    }

    override fun getFavoritesByCountry(countryId: String): Flow<List<Product>> {
        return favoriteProductDao.getFavoritesByCountry(countryId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllFavorites(): Flow<List<Product>> {
        return favoriteProductDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun isFavorite(productId: String): Flow<Boolean> {
        return favoriteProductDao.isFavorite(productId)
    }

    private fun Product.toEntity(countryId: String): FavoriteProductEntity {
        return FavoriteProductEntity(
            id = id,
            title = title,
            price = price,
            description = description,
            category = category,
            imageUrl = imageUrl,
            ratingRate = rating?.rate,
            ratingCount = rating?.count,
            countryId = countryId
        )
    }

    private fun FavoriteProductEntity.toDomain(): Product {
        val rating = if (ratingRate != null && ratingCount != null) {
            Rating(ratingRate, ratingCount)
        } else {
            null
        }

        return Product(
            id = id,
            title = title,
            price = price,
            description = description,
            category = category,
            imageUrl = imageUrl,
            rating = rating
        )
    }
} 