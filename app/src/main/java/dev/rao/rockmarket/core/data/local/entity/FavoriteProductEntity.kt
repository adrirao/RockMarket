package dev.rao.rockmarket.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProductEntity(
    @PrimaryKey val id: String,
    val title: String,
    val price: Double,
    val description: String,
    val category: String?,
    val imageUrl: String,
    val ratingRate: Double?,
    val ratingCount: Int?,
    val countryId: String // Para filtrar por país
) 