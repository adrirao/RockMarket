package dev.rao.rockmarket.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.rao.rockmarket.core.data.local.dao.FavoriteProductDao
import dev.rao.rockmarket.core.data.local.entity.FavoriteProductEntity

@Database(
    entities = [FavoriteProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RockMarketDatabase : RoomDatabase() {

    abstract fun favoriteProductDao(): FavoriteProductDao

    companion object {
        @Volatile
        private var INSTANCE: RockMarketDatabase? = null

        fun getDatabase(context: Context): RockMarketDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RockMarketDatabase::class.java,
                    "rockmarket_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 