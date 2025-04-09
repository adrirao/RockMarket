package dev.rao.rockmarket.core.data.remote

import dev.rao.rockmarket.core.data.remote.dto.PlatziProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PlatziApi {
    @GET("products")
    suspend fun getProducts(): List<PlatziProductDto>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): PlatziProductDto
    companion object {
        const val BASE_URL = "https://api.escuelajs.co/api/v1/"
    }
}