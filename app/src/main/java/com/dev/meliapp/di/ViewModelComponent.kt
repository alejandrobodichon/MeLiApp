package com.dev.meliapp.di

import com.dev.meliapp.viewmodel.DetailViewModel
import com.dev.meliapp.viewmodel.SearchViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules=[ApiModule::class, AppModule::class])
interface ViewModelComponent {
    fun inject(viewModel: SearchViewModel)
    fun inject(viewModel: DetailViewModel)
}