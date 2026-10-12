package com.example.repaso.domain.useCases

import com.example.repaso.data.UsuarioRepository

class DameListaUseCase {
    fun dameLista()= UsuarioRepository.dameLista()
}