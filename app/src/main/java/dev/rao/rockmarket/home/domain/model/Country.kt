package dev.rao.rockmarket.home.domain.model

data class Country(
    val id: String,
    val name: String,
    val code: String,
    val flagUrl: String? = null
) 