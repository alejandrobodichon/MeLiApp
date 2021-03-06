package com.dev.meliapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dev.meliapp.di.AppModule
import com.dev.meliapp.di.DaggerViewModelComponent
import com.dev.meliapp.model.ApiResult
import com.dev.meliapp.model.ProductModel
import com.dev.meliapp.services.ProductsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, test: Boolean = true):this(application) {
        injected = true
    }

    val products by lazy { MutableLiveData<List<ProductModel>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()
    private var injected = false

    @Inject
    lateinit var api: ProductsApiService

    fun inject() {
        if (!injected) {
            DaggerViewModelComponent.builder()
                .build()
                .inject(this)
        }
    }

    fun retrieveProductsByName(name: String) {
        inject()
        loading.value = true
        getProducts(name)
    }

    private fun getProducts(name: String) {
        disposable.add(
            api.getProducts(name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiResult>() {
                    override fun onSuccess(result: ApiResult) {
                        loadError.value = false
                        products.value = result.results
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        products.value = null
                        loadError.value = true
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}