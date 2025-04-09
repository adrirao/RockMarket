package dev.rao.rockmarket.core.data.remote

import dev.rao.rockmarket.core.data.remote.mapper.toProduct
import dev.rao.rockmarket.core.domain.model.Product
import javax.inject.Inject

class PlatziService @Inject constructor(
    private val api: PlatziApi
) : ProductService {

    override suspend fun getProducts(): List<Product> {
        return api.getProducts().map { dto ->
            dto.toProduct()
        }
    }

    override suspend fun getProductById(id: String): Product? {
        return try {
            api.getProductById(id).toProduct()
        } catch (e: Exception) {
            null
        }
    }
}