package dev.rao.rockmarket.detail_product.presentation

import dev.rao.rockmarket.core.domain.model.Product

sealed class ProductDetailState {
    object Loading : ProductDetailState()
    data class Success(val product: Product) : ProductDetailState()
    data class Error(val message: String) : ProductDetailState()
} 