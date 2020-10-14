package com.dev.meliapp.services

import com.dev.meliapp.di.DaggerApiComponent
import com.dev.meliapp.model.ApiResult
import com.dev.meliapp.model.ProductModel
import io.reactivex.Single
import javax.inject.Inject


class ProductsApiService {

    @Inject
    lateinit var api: ProductApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getProducts(name: String): Single<ApiResult> {
        return api.getProductsByName(name)
    }

    fun getProductDetail(id: String): Single<ProductModel> {
        return api.getProductDetail(id)
    }


}