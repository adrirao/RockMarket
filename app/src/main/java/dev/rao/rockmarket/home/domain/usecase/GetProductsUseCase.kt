package dev.rao.rockmarket.home.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import dev.rao.rockmarket.core.domain.model.Product
import dev.rao.rockmarket.core.domain.repository.ProductRepository
import javax.inject.Inject

@ViewModelScoped
class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(countryCode: String): Result<List<Product>> {
        return productRepository.getProducts(countryCode)
    }
} 