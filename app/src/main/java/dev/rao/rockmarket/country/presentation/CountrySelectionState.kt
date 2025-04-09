package dev.rao.rockmarket.country.presentation

import dev.rao.rockmarket.core.domain.model.Country

sealed class CountrySelectionState {
    object Initial : CountrySelectionState()
    object Loading : CountrySelectionState()
    data class Success(
        val countries: List<Country>,
        val selectedCountry: Country? = null
    ) : CountrySelectionState()

    data class Error(val message: String) : CountrySelectionState()
} 