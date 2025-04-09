package dev.rao.rockmarket.detail_product.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rao.rockmarket.core.domain.repository.ProductRepository
import dev.rao.rockmarket.detail_product.domain.usecase.GetProductByIdUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetailProductModule {
    @Provides
    @Singleton
    fun provideGetProductByIdUseCase(
        productRepository: ProductRepository
    ): GetProductByIdUseCase = GetProductByIdUseCase(productRepository)
}