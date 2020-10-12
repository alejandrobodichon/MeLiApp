package com.dev.meliapp.services

import com.dev.meliapp.model.ApiResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("sites/MLA/search")
    fun getProductsByName(@Query("q") name: String): Single<ApiResult>

    @GET("items/{productId}")
    fun getProductDetail(@Path("productId") productId: String): Single<ApiResult>
}