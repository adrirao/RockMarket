package dev.rao.rockmarket.core.data.repository

import dev.rao.rockmarket.core.data.remote.ProductServiceFactory
import dev.rao.rockmarket.core.domain.model.Product
import dev.rao.rockmarket.core.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val productServiceFactory: ProductServiceFactory
) : ProductRepository {
    override suspend fun getProducts(countryCode: String): Result<List<Product>> {
        return try {
            val service = productServiceFactory.createService(countryCode)
            val products = service.getProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(countryCode: String, productId: String): Result<Product> {
        return try {
            val service = productServiceFactory.createService(countryCode)
            val product = service.getProductById(productId)
                ?: throw NoSuchElementException("No se encontró el producto con ID: $productId")
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}