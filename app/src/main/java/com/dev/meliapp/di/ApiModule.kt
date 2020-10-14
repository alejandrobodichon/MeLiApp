package com.dev.meliapp.di

import com.dev.meliapp.services.ProductApi
import com.dev.meliapp.services.ProductsApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ApiModule {

    @Provides
    fun provideProductApi(): ProductApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }

    @Provides
    open fun provideProductApiService(): ProductsApiService{
        return ProductsApiService()
    }

    companion object {
        const val BASE_URL = "https://api.mercadolibre.com/"
    }
}