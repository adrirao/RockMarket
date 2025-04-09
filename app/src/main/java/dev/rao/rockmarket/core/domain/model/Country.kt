package dev.rao.rockmarket.core.domain.model

data class Country(
    val id: String,
    val name: String,
    val code: String,
    val flagUrl: String? = null,
    val currencySymbol: String
) 