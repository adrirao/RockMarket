package dev.rao.rockmarket.country.data.repository

import dev.rao.rockmarket.core.domain.model.Country

object CountryProvider {
    const val KEY_SELECTED_COUNTRY = "selected_country"
    fun provideCountries(): List<Country> = listOf(
        Country("AR", "Argentina", "País de Argentina", "https://flagcdn.com/w320/ar.png", "\$"),
        Country("BR", "Brasil", "País de Brasil", "https://flagcdn.com/w320/br.png", "R\$"),
    )
}