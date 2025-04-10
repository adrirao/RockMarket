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
import dev.rao.rockmarket.home.domain.usecase.CheckFavoriteStatusUseCase
import dev.rao.rockmarket.home.domain.usecase.GetFavoriteProductsUseCase
import dev.rao.rockmarket.home.domain.usecase.GetProductsUseCase
import dev.rao.rockmarket.home.domain.usecase.ToggleFavoriteProductUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getSelectedCountryUseCase: GetSelectedCountryUseCase,
    private val saveSelectedCountryUseCase: SaveSelectedCountryUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    private val toggleFavoriteProductUseCase: ToggleFavoriteProductUseCase,
    private val checkFavoriteStatusUseCase: CheckFavoriteStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Initial)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts: StateFlow<List<Product>> = _favoriteProducts.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isShowingFavorites = MutableStateFlow(false)
    val isShowingFavorites: StateFlow<Boolean> = _isShowingFavorites.asStateFlow()

    private val _productFavoriteStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val productFavoriteStatus: StateFlow<Map<String, Boolean>> =
        _productFavoriteStatus.asStateFlow()

    init {
        loadSelectedCountry()
    }

    private fun loadSelectedCountry() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getSelectedCountryUseCase().collect { country ->
                    _state.value = HomeState.Success(country)

                    country?.let {
                        loadFeaturedProducts(it)
                        loadFavoriteProducts(it.id)
                    }
                }
            }
        }
    }

    private fun loadFavoriteProducts(countryId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getFavoriteProductsUseCase(countryId).collectLatest { favorites ->
                    _favoriteProducts.value = favorites

                    // Actualizamos el mapa de estado de los favoritos
                    val currentMap = _productFavoriteStatus.value.toMutableMap()
                    favorites.forEach { product ->
                        currentMap[product.id] = true
                    }
                    _productFavoriteStatus.value = currentMap
                }
            }
        }
    }

    fun toggleShowFavorites() {
        _isShowingFavorites.value = !_isShowingFavorites.value
    }

    fun toggleFavorite(product: Product) {
        val currentState = _state.value
        if (currentState is HomeState.Success && currentState.country != null) {
            viewModelScope.launch {
                toggleFavoriteProductUseCase(product, currentState.country.id)

                // Actualizar inmediatamente el estado de favorito en la UI
                val currentMap = _productFavoriteStatus.value.toMutableMap()
                val newStatus = !(currentMap[product.id] ?: false)
                currentMap[product.id] = newStatus
                _productFavoriteStatus.value = currentMap
            }
        }
    }

    fun checkFavoriteStatus(productId: String) {
        viewModelScope.launch {
            checkFavoriteStatusUseCase(productId).collectLatest { isFavorite ->
                val currentMap = _productFavoriteStatus.value.toMutableMap()
                currentMap[productId] = isFavorite
                _productFavoriteStatus.value = currentMap
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
            withContext(Dispatchers.IO) {
                getProductsUseCase(country.id).onSuccess {
                    _products.value = it

                    // Verificar el estado de favorito para cada producto
                    it.forEach { product ->
                        checkFavoriteStatus(product.id)
                    }
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

    fun refreshProducts() {
        val currentState = _state.value
        _isRefreshing.value = true

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Obtener el país seleccionado sin importar el estado actual
                getSelectedCountryUseCase().collect { country ->
                    if (country != null) {
                        getProductsUseCase(country.id)
                            .onSuccess {
                                _products.value = it
                                _state.value = HomeState.Success(country)

                                // Verificar el estado de favorito para cada producto
                                it.forEach { product ->
                                    checkFavoriteStatus(product.id)
                                }
                            }
                            .onFailure { exception ->
                                Log.e("HomeViewModel", "Error al recargar los productos", exception)
                                val message = if (NetworkUtils.isNetworkConnected(context)) {
                                    "Error al recargar los productos. Por favor, inténtalo más tarde."
                                } else {
                                    "No hay conexión a internet"
                                }
                                _state.value = HomeState.Error(message)
                            }
                        _isRefreshing.value = false
                        return@collect // Terminar la recolección después de procesar el país
                    } else {
                        _state.value = HomeState.Error("No se ha seleccionado ningún país")
                        _isRefreshing.value = false
                    }
                }
            }
        }
    }
}

