package com.example.examen_mvvm_alvarosantos.domain.useCases

import com.example.examen_mvvm_alvarosantos.data.UsuarioRepository
import com.example.examen_mvvm_alvarosantos.domain.model.Usuario


class DeleteUseCase {
    fun delete(usuario: Usuario){
        UsuarioRepository.delete(usuario)
    }
}

