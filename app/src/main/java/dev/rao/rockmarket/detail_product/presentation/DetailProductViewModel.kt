package dev.rao.rockmarket.detail_product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rao.rockmarket.country.domain.usecase.GetSelectedCountryUseCase
import dev.rao.rockmarket.detail_product.domain.usecase.GetProductByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getSelectedCountryUseCase: GetSelectedCountryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    fun loadProductDetail(productId: String) {
        viewModelScope.launch {
            _state.value = ProductDetailState.Loading

            try {
                // Obtener el país seleccionado
                val country = getSelectedCountryUseCase().first()

                if (country == null) {
                    _state.value = ProductDetailState.Error("No se ha seleccionado ningún país")
                    return@launch
                }

                // Obtener el producto
                getProductByIdUseCase(country.id, productId)
                    .onSuccess { product ->
                        _state.value = ProductDetailState.Success(product)
                    }
                    .onFailure { error ->
                        _state.value = ProductDetailState.Error(
                            error.message ?: "Error desconocido"
                        )
                    }
            } catch (e: Exception) {
                _state.value = ProductDetailState.Error(
                    e.message ?: "Error desconocido"
                )
            }
        }
    }
}

