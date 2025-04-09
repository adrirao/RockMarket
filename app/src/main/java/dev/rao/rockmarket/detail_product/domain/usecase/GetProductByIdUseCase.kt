package dev.rao.rockmarket.detail_product.domain.usecase

import dev.rao.rockmarket.core.domain.model.Product
import dev.rao.rockmarket.core.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(countryCode: String, productId: String): Result<Product> {
        return productRepository.getProductById(countryCode, productId)
    }
}