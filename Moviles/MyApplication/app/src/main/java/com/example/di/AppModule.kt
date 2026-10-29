package com.example.di

import com.example.domain.useCases.DameListaUseCase
import com.example.domain.useCases.DameUsuarioUseCase
import com.example.domain.useCases.DeleteUseCase
import com.example.domain.useCases.GuardarUseCase

object AppModule {

    val guardarUseCase = GuardarUseCase()
    val dameListaUseCase = DameListaUseCase()
    val deleteUseCase = DeleteUseCase()
    val dameUsuarioUseCase = DameUsuarioUseCase()
}