package dev.rao.rockmarket.home.data.remote

import dev.rao.rockmarket.home.data.remote.mapper.toProduct
import dev.rao.rockmarket.home.domain.model.Product
import javax.inject.Inject

class FakeStoreService @Inject constructor(
    private val api: FakeStoreApi
) : ProductService {

    override suspend fun getProducts(): List<Product> {
        return api.getProducts().map { dto ->
            dto.toProduct()
        }
    }
} 