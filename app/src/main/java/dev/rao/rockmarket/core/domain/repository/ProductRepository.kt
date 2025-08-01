package dev.rao.rockmarket.core.domain.repository

import dev.rao.rockmarket.core.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(countryCode: String): Result<List<Product>>
    suspend fun getProductById(countryCode: String, productId: String): Result<Product>
}