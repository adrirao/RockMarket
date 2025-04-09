package dev.rao.rockmarket.core.data.remote

import dev.rao.rockmarket.core.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): ProductDto

    companion object {
        const val BASE_URL = "https://fakestoreapi.com/"
    }
} 