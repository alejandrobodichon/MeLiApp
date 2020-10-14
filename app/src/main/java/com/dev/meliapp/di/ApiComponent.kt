package com.dev.meliapp.di

import com.dev.meliapp.services.ProductApi
import com.dev.meliapp.services.ProductsApiService
import dagger.Component

@Component(modules=[ApiModule::class])
interface ApiComponent {

    fun inject(service: ProductsApiService)
}