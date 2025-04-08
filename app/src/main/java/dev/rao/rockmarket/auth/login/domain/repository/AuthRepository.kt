package dev.rao.rockmarket.auth.login.domain.repository

import com.google.firebase.auth.AuthCredential
import dev.rao.rockmarket.auth.login.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>
    suspend fun signInWithGoogle(credential: AuthCredential): Result<User>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): Flow<User?>
    fun isUserLoggedIn(): Boolean
} 