package dev.rao.rockmarket.auth.login.domain.model

data class User(
    val id: String,
    val email: String,
    val displayName: String? = null,
    val photoUrl: String? = null
) 