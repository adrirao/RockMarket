package dev.rao.rockmarket.home.data.remote

import dev.rao.rockmarket.home.data.remote.dto.PlatziProductDto
import retrofit2.http.GET

interface PlatziApi {
    @GET("products")
    suspend fun getProducts(): List<PlatziProductDto>

    companion object {
        const val BASE_URL = "https://api.escuelajs.co/api/v1/"
    }
}