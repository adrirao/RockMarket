package dev.rao.rockmarket.core.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rao.rockmarket.core.data.remote.FakeStoreApi
import dev.rao.rockmarket.core.data.remote.FakeStoreService
import dev.rao.rockmarket.core.data.remote.PlatziApi
import dev.rao.rockmarket.core.data.remote.PlatziService
import dev.rao.rockmarket.core.data.remote.ProductServiceFactory
import dev.rao.rockmarket.core.data.repository.ProductRepositoryImpl
import dev.rao.rockmarket.core.domain.repository.ProductRepository
import dev.rao.rockmarket.home.domain.usecase.GetProductsUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    @Named("fakeStore")
    fun provideFakeStoreRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FakeStoreApi.Companion.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
    }

    @Provides
    @Singleton
    @Named("platzi")
    fun providePlatziRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PlatziApi.Companion.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
    }

    @Provides
    @Singleton
    fun provideFakeStoreApi(@Named("fakeStore") retrofit: Retrofit): FakeStoreApi {
        return retrofit.create(FakeStoreApi::class.java)
    }

    @Provides
    @Singleton
    fun providePlatziApi(@Named("platzi") retrofit: Retrofit): PlatziApi {
        return retrofit.create(PlatziApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductServiceFactory(
        fakeStoreService: FakeStoreService,
        platziService: PlatziService
    ): ProductServiceFactory {
        return ProductServiceFactory(fakeStoreService, platziService)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        productServiceFactory: ProductServiceFactory
    ): ProductRepository = ProductRepositoryImpl(productServiceFactory)

    @Provides
    @Singleton
    fun provideGetProductsUseCase(
        productRepository: ProductRepository
    ): GetProductsUseCase = GetProductsUseCase(productRepository)
}