package dev.rao.rockmarket.home.data.remote

import dev.rao.rockmarket.home.data.remote.mapper.toProduct
import dev.rao.rockmarket.home.domain.model.Product
import javax.inject.Inject

class PlatziService @Inject constructor(
    private val api: PlatziApi
) : ProductService {

    override suspend fun getProducts(): List<Product> {
        return api.getProducts().map { dto ->
            dto.toProduct()
        }
    }
} 