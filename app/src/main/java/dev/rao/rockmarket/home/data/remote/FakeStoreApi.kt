package dev.rao.rockmarket.home.data.remote

import dev.rao.rockmarket.home.data.remote.dto.ProductDto
import retrofit2.http.GET

interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    companion object {
        const val BASE_URL = "https://fakestoreapi.com/"
    }
} 