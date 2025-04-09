package dev.rao.rockmarket.home.domain.repository

import dev.rao.rockmarket.home.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(countryCode: String): Result<List<Product>>
}