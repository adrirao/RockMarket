package dev.rao.rockmarket.home.data.remote

import dev.rao.rockmarket.home.domain.model.Product

interface ProductService {
    suspend fun getProducts(): List<Product>

    companion object {
        const val COUNTRY_A = "AR"
        const val COUNTRY_B = "BR"
    }
} 