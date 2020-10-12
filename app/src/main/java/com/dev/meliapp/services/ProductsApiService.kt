package com.dev.meliapp.services

import com.dev.meliapp.model.ApiResult
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ProductsApiService {
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ProductApi::class.java)

    fun getProducts(name: String): Single<ApiResult> {
        return api.getProductsByName(name)
    }


    companion object {
        const val BASE_URL = "https://api.mercadolibre.com/"
    }
}