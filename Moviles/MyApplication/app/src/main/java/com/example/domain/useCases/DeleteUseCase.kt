package com.example.domain.useCases

import com.example.data.UsuarioRepository
import com.example.domain.model.Usuario

class DeleteUseCase {

    fun delete(usuario: Usuario) {
        UsuarioRepository.delete(usuario)
    }
}