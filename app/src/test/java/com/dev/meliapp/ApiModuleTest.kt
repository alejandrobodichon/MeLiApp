package com.dev.meliapp

import com.dev.meliapp.di.ApiModule
import com.dev.meliapp.services.ProductsApiService

class ApiModuleTest(val mockService: ProductsApiService): ApiModule() {
    override fun provideProductApiService(): ProductsApiService{
        return mockService
    }

}