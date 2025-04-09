package dev.rao.rockmarket.country.domain.repository

import dev.rao.rockmarket.core.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getAllCountries(): Flow<List<Country>>
    suspend fun getCountryById(id: String): Result<Country>
    suspend fun getCountryByCode(code: String): Result<Country>
    suspend fun saveSelectedCountry(country: Country?): Result<Unit>
    fun getSelectedCountry(): Flow<Country?>
}