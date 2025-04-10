package dev.rao.rockmarket.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rao.rockmarket.core.data.repository.FavoriteRepositoryImpl
import dev.rao.rockmarket.core.domain.repository.FavoriteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteRepositoryImpl: FavoriteRepositoryImpl): FavoriteRepository {
        return favoriteRepositoryImpl
    }
} 