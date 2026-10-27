package com.example.repaso.domain.useCases

import com.example.repaso.data.UsuarioRepository

class DameUsuarioUseCase {
    fun dameUsuario() = UsuarioRepository.dameUsuario()
}