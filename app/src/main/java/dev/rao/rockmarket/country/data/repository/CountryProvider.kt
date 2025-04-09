package dev.rao.rockmarket.country.data.repository

import dev.rao.rockmarket.country.domain.model.Country

object CountryProvider {
    const val KEY_SELECTED_COUNTRY = "selected_country"
    fun provideCountries(): List<Country> = listOf(
        Country("AR", "Argentina", "País de Argentina", "https://example.com/argentina.png"),
        Country("BR", "Brasil", "País de Brasil", "https://example.com/brasil.png"),
    )
}