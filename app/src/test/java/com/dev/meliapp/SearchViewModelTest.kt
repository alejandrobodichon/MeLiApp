package com.dev.meliapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dev.meliapp.di.DaggerViewModelComponent
import com.dev.meliapp.model.ApiResult
import com.dev.meliapp.model.ProductModel
import com.dev.meliapp.services.ProductsApiService
import com.dev.meliapp.viewmodel.SearchViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class SearchViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var productService: ProductsApiService

    val application = Mockito.mock(Application::class.java)
    var searchViewModel = SearchViewModel(application, true)

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent.builder()
            .apiModule(ApiModuleTest(productService))
            .build()
            .inject(searchViewModel)
    }

    @Test
    fun getProductsSuccess(){
        val product = ProductModel("1","car",10.5f,"2","")
        val productList = listOf(product)
        val apiResult = ApiResult(productList,"")
        val testSingle = Single.just(apiResult)
        Mockito.`when`(productService.getProducts("car")).thenReturn(testSingle)
        searchViewModel.retrieveProductsByName("car")
        Assert.assertEquals(1, searchViewModel.products.value?.size)
        Assert.assertEquals(false, searchViewModel.loadError.value)
        Assert.assertEquals(false, searchViewModel.loading.value)
    }

    @Test
    fun getProductsFailure(){
        val testSingle = Single.error<ApiResult>(Throwable())
        Mockito.`when`(productService.getProducts("car")).thenReturn(testSingle)
        searchViewModel.retrieveProductsByName("car")
        Assert.assertEquals(null, searchViewModel.products.value)
        Assert.assertEquals(true, searchViewModel.loadError.value)
        Assert.assertEquals(false, searchViewModel.loading.value)
    }

    @Before
    fun setupRxSchedulers(){
        val inmediate = object: Scheduler(){
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() },true)
            }
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> inmediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> inmediate }
    }
}