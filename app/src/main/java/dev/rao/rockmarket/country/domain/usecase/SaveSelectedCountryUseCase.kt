package dev.rao.rockmarket.country.domain.usecase

import dev.rao.rockmarket.country.domain.model.Country
import dev.rao.rockmarket.country.domain.repository.CountryRepository
import javax.inject.Inject

class SaveSelectedCountryUseCase @Inject constructor(
    private val countryRepository: CountryRepository
) {
    suspend operator fun invoke(country: Country): Result<Unit> =
        countryRepository.saveSelectedCountry(country)
} 