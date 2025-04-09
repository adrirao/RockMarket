package dev.rao.rockmarket.country.domain.usecase

import dev.rao.rockmarket.core.domain.model.Country
import dev.rao.rockmarket.country.domain.repository.CountryRepository
import javax.inject.Inject

class GetCountryByCodeUseCase @Inject constructor(
    private val countryRepository: CountryRepository
) {
    suspend operator fun invoke(code: String): Country? {
        return countryRepository.getCountryByCode(code).getOrNull()
    }
} 