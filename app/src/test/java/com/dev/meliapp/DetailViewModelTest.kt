package com.dev.meliapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dev.meliapp.di.DaggerViewModelComponent
import com.dev.meliapp.model.ProductModel
import com.dev.meliapp.services.ProductsApiService
import com.dev.meliapp.viewmodel.DetailViewModel
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

class DetailViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var productService: ProductsApiService

    val application = Mockito.mock(Application::class.java)
    var detailViewModel = DetailViewModel(application, true)

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent.builder()
            .apiModule(ApiModuleTest(productService))
            .build()
            .inject(detailViewModel)
    }

    @Test
    fun getProductsSuccess(){
        val product = ProductModel("1","car",10.5f,"2","")
        val testSingle = Single.just(product)
        Mockito.`when`(productService.getProductDetail("1")).thenReturn(testSingle)
        detailViewModel.retrieveProductDetail("1")
        Assert.assertEquals(ProductModel("1","car",10.5f,"2",""), detailViewModel.productDetail.value)
        Assert.assertEquals(false, detailViewModel.loadError.value)
        Assert.assertEquals(false, detailViewModel.loading.value)
    }

    @Test
    fun getProductsFailure(){
        val testSingle = Single.error<ProductModel>(Throwable())
        Mockito.`when`(productService.getProductDetail("1")).thenReturn(testSingle)
        detailViewModel.retrieveProductDetail("1")
        Assert.assertEquals(null, detailViewModel.productDetail.value)
        Assert.assertEquals(true, detailViewModel.loadError.value)
        Assert.assertEquals(false, detailViewModel.loading.value)
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