package com.example.mvvm.di

import com.example.mvvm.domain.useCases.DamePresidenteUseCase
import com.example.mvvm.ui.main.MainViewModel

object AppModule {

fun provideViewModel(): MainViewModel = MainViewModel(damePresidenteUseCase)


    val damePresidenteUseCase: DamePresidenteUseCase = DamePresidenteUseCase()

}