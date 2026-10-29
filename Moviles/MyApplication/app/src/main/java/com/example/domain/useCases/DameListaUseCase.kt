package com.example.domain.useCases

import com.example.data.UsuarioRepository

class DameListaUseCase {

    fun dameLista() = UsuarioRepository.dameLista()
}