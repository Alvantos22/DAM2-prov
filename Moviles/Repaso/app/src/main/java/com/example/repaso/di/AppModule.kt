package com.example.repaso.di

import com.example.repaso.domain.useCases.DameListaUseCase
import com.example.repaso.domain.useCases.DameUsuarioUseCase
import com.example.repaso.domain.useCases.DeleteUseCase
import com.example.repaso.domain.useCases.GuardarUseCase

object AppModule {
    val guardarUseCase = GuardarUseCase()
    val dameListaUseCase = DameListaUseCase()
    val deleteUseCase = DeleteUseCase()
    val dameUsuarioUseCase = DameUsuarioUseCase()
}