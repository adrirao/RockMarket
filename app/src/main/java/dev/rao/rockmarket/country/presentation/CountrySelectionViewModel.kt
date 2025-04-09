package dev.rao.rockmarket.country.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rao.rockmarket.core.domain.model.Country
import dev.rao.rockmarket.country.domain.usecase.GetCountriesUseCase
import dev.rao.rockmarket.country.domain.usecase.GetSelectedCountryUseCase
import dev.rao.rockmarket.country.domain.usecase.SaveSelectedCountryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountrySelectionViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val saveSelectedCountryUseCase: SaveSelectedCountryUseCase,
    private val getSelectedCountryUseCase: GetSelectedCountryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CountrySelectionState>(CountrySelectionState.Initial)
    val state: StateFlow<CountrySelectionState> = _state.asStateFlow()

    init {
        loadCountries()
        observeSelectedCountry()
    }

    private fun loadCountries() {
        viewModelScope.launch {
            try {
                getCountriesUseCase().collect { countries ->
                    _state.value = CountrySelectionState.Success(countries)
                }
            } catch (e: Exception) {
                _state.value = CountrySelectionState.Error("Error al cargar los países")
            }
        }
    }

    private fun observeSelectedCountry() {
        viewModelScope.launch {
            getSelectedCountryUseCase().collect { country ->
                _state.value = when (val currentState = _state.value) {
                    is CountrySelectionState.Success -> currentState.copy(selectedCountry = country)
                    else -> currentState
                }
            }
        }
    }

    fun selectCountry(country: Country) {
        viewModelScope.launch {
            _state.value = CountrySelectionState.Loading
            saveSelectedCountryUseCase(country)
                .onSuccess {
                    _state.value = CountrySelectionState.Success(
                        countries = (state.value as? CountrySelectionState.Success)?.countries
                            ?: emptyList(),
                        selectedCountry = country
                    )
                }
                .onFailure {
                    _state.value =
                        CountrySelectionState.Error("Error al guardar el país seleccionado")
                }
        }
    }
}