package dev.rao.rockmarket.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rao.rockmarket.auth.login.domain.usecase.SignOutUseCase
import dev.rao.rockmarket.country.domain.model.Country
import dev.rao.rockmarket.country.domain.usecase.GetSelectedCountryUseCase
import dev.rao.rockmarket.country.domain.usecase.SaveSelectedCountryUseCase
import dev.rao.rockmarket.home.domain.model.FeaturedProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSelectedCountryUseCase: GetSelectedCountryUseCase,
    private val saveSelectedCountryUseCase: SaveSelectedCountryUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Initial)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _featuredProducts = MutableStateFlow<List<FeaturedProduct>>(emptyList())
    val featuredProducts: StateFlow<List<FeaturedProduct>> = _featuredProducts.asStateFlow()

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
        val products = when (country.id) {
            "0" -> getMockFeaturedProductsForCountryA()
            "1" -> getMockFeaturedProductsForCountryB()
            else -> emptyList()
        }

        _featuredProducts.value = products
    }

    private fun getMockFeaturedProductsForCountryA(): List<FeaturedProduct> {
        return listOf(
            FeaturedProduct(
                "1",
                "Smartphone Galaxy S21",
                799.99,
                "https://img.pacifiko.com/PROD/resize/1/1000x1000/YWU1YzEzY2_4.jpg"
            ),
            FeaturedProduct(
                "2",
                "Audífonos Bluetooth",
                59.99,
                "https://img.pacifiko.com/PROD/resize/1/1000x1000/YWU1YzEzY2_4.jpg"
            ),
            FeaturedProduct(
                "3",
                "Laptop Pro 15",
                1299.99,
                "https://img.pacifiko.com/PROD/resize/1/1000x1000/YWU1YzEzY2_4.jpg"
            ),
            FeaturedProduct(
                "4",
                "Smartwatch Serie 7",
                349.99,
                "https://img.pacifiko.com/PROD/resize/1/1000x1000/YWU1YzEzY2_4.jpg"
            ),
            FeaturedProduct(
                "5",
                "Tablet Air",
                499.99,
                "https://img.pacifiko.com/PROD/resize/1/1000x1000/YWU1YzEzY2_4.jpg"
            )
        )
    }

    private fun getMockFeaturedProductsForCountryB(): List<FeaturedProduct> {
        return listOf(
            FeaturedProduct(
                "6",
                "iPhone 13 Pro",
                999.99,
                "https://via.placeholder.com/150/FFC107/FFFFFF?text=iPhone+13"
            ),
            FeaturedProduct(
                "7",
                "AirPods Pro",
                249.99,
                "https://via.placeholder.com/150/FF9800/FFFFFF?text=AirPods"
            ),
            FeaturedProduct(
                "8",
                "MacBook Air",
                999.99,
                "https://via.placeholder.com/150/FFC107/FFFFFF?text=MacBook"
            ),
            FeaturedProduct(
                "9",
                "Apple Watch",
                399.99,
                "https://via.placeholder.com/150/FF9800/FFFFFF?text=Watch"
            ),
            FeaturedProduct(
                "10",
                "iPad Pro",
                799.99,
                "https://via.placeholder.com/150/FFC107/FFFFFF?text=iPad"
            )
        )
    }
}

sealed class HomeState {
    object Initial : HomeState()
    data class Success(val country: Country?) : HomeState()
    data class Error(val message: String) : HomeState()
}