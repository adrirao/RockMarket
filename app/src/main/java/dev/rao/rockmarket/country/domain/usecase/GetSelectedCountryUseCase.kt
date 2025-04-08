package dev.rao.rockmarket.country.domain.usecase

import dev.rao.rockmarket.country.domain.model.Country
import dev.rao.rockmarket.country.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSelectedCountryUseCase @Inject constructor(
    private val countryRepository: CountryRepository
) {
    operator fun invoke(): Flow<Country?> = countryRepository.getSelectedCountry()
} 