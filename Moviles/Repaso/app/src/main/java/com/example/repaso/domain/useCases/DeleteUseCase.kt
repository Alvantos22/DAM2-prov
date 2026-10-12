package com.example.repaso.domain.useCases

import com.example.repaso.data.UsuarioRepository
import com.example.repaso.domain.model.Usuario

class DeleteUseCase {
    fun delete(usuario: Usuario){
        UsuarioRepository.delete(usuario)
    }
}

