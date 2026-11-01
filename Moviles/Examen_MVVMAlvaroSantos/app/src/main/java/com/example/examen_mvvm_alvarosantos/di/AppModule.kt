package com.example.examen_mvvm_alvarosantos.di

import com.example.examen_mvvm_alvarosantos.domain.useCases.DameListaUseCase
import com.example.examen_mvvm_alvarosantos.domain.useCases.DeleteUseCase
import com.example.examen_mvvm_alvarosantos.domain.useCases.GuardarUseCase


object AppModule {
    val guardarUseCase = GuardarUseCase()
    val dameListaUseCase = DameListaUseCase()
    val deleteUseCase = DeleteUseCase()
}