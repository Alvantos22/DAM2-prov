package com.example.domain.useCases

import com.example.data.UsuarioRepository;

class DameUsuarioUseCase {

    fun dameUsuario() = UsuarioRepository.dameUsuario()
}