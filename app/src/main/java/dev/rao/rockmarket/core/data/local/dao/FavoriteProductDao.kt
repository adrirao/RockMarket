package dev.rao.rockmarket.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.rao.rockmarket.core.data.local.entity.FavoriteProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(product: FavoriteProductEntity)

    @Query("DELETE FROM favorite_products WHERE id = :productId")
    suspend fun removeFavorite(productId: String)

    @Query("SELECT * FROM favorite_products WHERE countryId = :countryId")
    fun getFavoritesByCountry(countryId: String): Flow<List<FavoriteProductEntity>>

    @Query("SELECT * FROM favorite_products")
    fun getAllFavorites(): Flow<List<FavoriteProductEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_products WHERE id = :productId)")
    fun isFavorite(productId: String): Flow<Boolean>
} 