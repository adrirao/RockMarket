package dev.rao.rockmarket.home.data.remote

import javax.inject.Inject

class ProductServiceFactory @Inject constructor(
    private val fakeStoreService: FakeStoreService,
    private val platziService: PlatziService
) {

    fun createService(countryCode: String): ProductService {
        return when (countryCode) {
            ProductService.COUNTRY_A -> fakeStoreService
            ProductService.COUNTRY_B -> platziService
            else -> throw IllegalArgumentException("País no soportado: $countryCode")
        }
    }
} 