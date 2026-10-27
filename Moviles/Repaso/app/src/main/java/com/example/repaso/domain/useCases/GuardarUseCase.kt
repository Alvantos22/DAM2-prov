package com.example.repaso.domain.useCases

import com.example.repaso.data.UsuarioRepository
import com.example.repaso.domain.model.Usuario

class GuardarUseCase {
    fun guardar(usuario: Usuario)=UsuarioRepository.guardar(usuario)
}