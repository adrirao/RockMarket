package dev.rao.rockmarket.home.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rao.rockmarket.home.domain.usecase.ValidateQrCodeUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScannerModule {

    @Provides
    @Singleton
    fun provideValidateQrCodeUseCase(): ValidateQrCodeUseCase {
        return ValidateQrCodeUseCase()
    }
} 