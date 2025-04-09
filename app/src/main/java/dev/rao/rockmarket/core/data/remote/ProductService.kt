package dev.rao.rockmarket.core.data.remote

import dev.rao.rockmarket.core.domain.model.Product

interface ProductService {
    suspend fun getProducts(): List<Product>
    suspend fun getProductById(id: String): Product?
    companion object {
        const val COUNTRY_A = "AR"
        const val COUNTRY_B = "BR"
    }
} 