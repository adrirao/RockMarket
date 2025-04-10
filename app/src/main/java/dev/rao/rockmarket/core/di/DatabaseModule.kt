package dev.rao.rockmarket.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.rao.rockmarket.core.data.local.RockMarketDatabase
import dev.rao.rockmarket.core.data.local.dao.FavoriteProductDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RockMarketDatabase {
        return RockMarketDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFavoriteProductDao(database: RockMarketDatabase): FavoriteProductDao {
        return database.favoriteProductDao()
    }
} 