package com.example.examen_mvvm_alvarosantos.domain.useCases

import com.example.examen_mvvm_alvarosantos.data.UsuarioRepository
import com.example.examen_mvvm_alvarosantos.domain.model.Usuario


class GuardarUseCase {
    fun guardar(usuario: Usuario)= UsuarioRepository.guardar(usuario)
}