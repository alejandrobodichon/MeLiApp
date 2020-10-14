package com.dev.meliapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dev.meliapp.di.DaggerViewModelComponent
import com.dev.meliapp.model.ProductModel
import com.dev.meliapp.services.ProductsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, test: Boolean = true):this(application) {
        injected = true
    }

    val productDetail by lazy { MutableLiveData<ProductModel>() }
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

    fun retrieveProductDetail(id: String) {
        inject()
        loading.value = true
        getProductDetail(id)
    }

    private fun getProductDetail(id: String) {
        disposable.add(
            api.getProductDetail(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProductModel>() {
                    override fun onSuccess(product: ProductModel) {
                        loadError.value = false
                        productDetail.value = product
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        productDetail.value = null
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