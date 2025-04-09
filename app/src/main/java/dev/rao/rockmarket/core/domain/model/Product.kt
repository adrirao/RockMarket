package dev.rao.rockmarket.core.domain.model

data class Product(
    val id: String,
    val title: String,
    val price: Double,
    val description: String,
    val category: String? = null,
    val imageUrl: String,
    val rating: Rating? = null
)

data class Rating(
    val rate: Double,
    val count: Int
) 