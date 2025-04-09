package dev.rao.rockmarket.home.presentation

import dev.rao.rockmarket.country.domain.model.Country

sealed class HomeState {
    object Initial : HomeState()
    data class Success(val country: Country?) : HomeState()
    data class Error(val message: String) : HomeState()
}