package dev.rao.rockmarket.home.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.rao.rockmarket.auth.login.domain.usecase.SignOutUseCase
import dev.rao.rockmarket.core.domain.model.Country
import dev.rao.rockmarket.core.domain.model.Product
import dev.rao.rockmarket.core.util.NetworkUtils
import dev.rao.rockmarket.country.domain.usecase.GetSelectedCountryUseCase
import dev.rao.rockmarket.country.domain.usecase.SaveSelectedCountryUseCase
import dev.rao.rockmarket.home.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getSelectedCountryUseCase: GetSelectedCountryUseCase,
    private val saveSelectedCountryUseCase: SaveSelectedCountryUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Initial)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        loadSelectedCountry()
    }

    private fun loadSelectedCountry() {
        viewModelScope.launch {
            getSelectedCountryUseCase().collect { country ->
                _state.value = HomeState.Success(country)

                country?.let {
                    loadFeaturedProducts(it)
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            saveSelectedCountryUseCase(null)
            signOutUseCase()
        }
    }

    private fun loadFeaturedProducts(country: Country) {
        viewModelScope.launch {
            getProductsUseCase(country.id).onSuccess {
                _products.value = it
            }.onFailure { exception ->
                Log.e("HomeViewModel", "Error al cargar los productos", exception)
                val message = if (NetworkUtils.isNetworkConnected(context)) {
                    "Error al cargar los productos. Por favor, inténtalo más tarde."
                } else {
                    "No hay conexión a internet"
                }
                _state.value = HomeState.Error(message)
            }
        }
    }
}

