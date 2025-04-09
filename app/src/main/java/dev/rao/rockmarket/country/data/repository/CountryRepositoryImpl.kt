package dev.rao.rockmarket.country.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import dev.rao.rockmarket.country.data.repository.CountryProvider.KEY_SELECTED_COUNTRY
import dev.rao.rockmarket.country.domain.model.Country
import dev.rao.rockmarket.country.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val countryProvider: CountryProvider,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : CountryRepository {
    override fun getAllCountries(): Flow<List<Country>> = flow {
        emit(countryProvider.provideCountries())
    }

    override suspend fun getCountryById(id: String): Result<Country> {
        return countryProvider.provideCountries().find { it.id == id }?.let {
            Result.success(it)
        } ?: Result.failure(Exception("País no encontrado"))
    }

    override suspend fun getCountryByCode(code: String): Result<Country> {
        return countryProvider.provideCountries().find { it.id == code }?.let {
            Result.success(it)
        } ?: Result.failure(Exception("País no encontrado"))
    }

    override suspend fun saveSelectedCountry(country: Country?): Result<Unit> {
        return try {
            sharedPreferences.edit()
                .putString(KEY_SELECTED_COUNTRY, gson.toJson(country ?: ""))
                .apply()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSelectedCountry(): Flow<Country?> = flow {
        val countryJson = sharedPreferences.getString(KEY_SELECTED_COUNTRY, null)
        val country = countryJson?.let {
            try {
                gson.fromJson(it, Country::class.java)
            } catch (e: Exception) {
                null
            }
        }
        emit(country)
    }

}