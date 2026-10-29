package com.example.domain.useCases

import com.example.data.UsuarioRepository
import com.example.domain.model.Usuario

class GuardarUseCase {

    fun guardar(usuario: Usuario) {
        require(usuario.nombre.isNotBlank()) {
            "El nombre no puede estar vacío"
        }

        UsuarioRepository.guardar(usuario)
    }
}