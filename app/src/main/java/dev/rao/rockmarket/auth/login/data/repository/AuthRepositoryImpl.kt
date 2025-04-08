package dev.rao.rockmarket.auth.login.data.repository

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.rao.rockmarket.auth.login.domain.model.User
import dev.rao.rockmarket.auth.login.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                Result.success(firebaseUser.toUser())
            } else {
                Result.failure(Exception("Error al iniciar sesión: usuario nulo"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Error al iniciar sesión", e)
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(credential: AuthCredential): Result<User> {
        return try {
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                Result.success(firebaseUser.toUser())
            } else {
                Result.failure(Exception("Error al iniciar sesión con Google: usuario nulo"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Error al iniciar sesión con Google", e)
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Error al cerrar sesión", e)
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser?.toUser()
            trySend(user)
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    private fun FirebaseUser.toUser(): User {
        return User(
            id = uid,
            email = email ?: "",
            displayName = displayName,
            photoUrl = photoUrl?.toString()
        )
    }
}