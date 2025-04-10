package dev.rao.rockmarket.detail_product.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.rao.rockmarket.core.util.NetworkUtils
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
    @ApplicationContext private val context: Context,
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
                        throw error
                    }
            } catch (e: Exception) {
                Log.e("DetailProductViewModel", "Error al obtener los detalles del producto", e)
                val message = if (NetworkUtils.isNetworkConnected(context)) {
                    "Error al obtener los detalles del producto. Por favor, inténtalo más tarde."
                } else {
                    "No hay conexión a internet"
                }
                _state.value = ProductDetailState.Error(message)
            }
        }
    }
}

