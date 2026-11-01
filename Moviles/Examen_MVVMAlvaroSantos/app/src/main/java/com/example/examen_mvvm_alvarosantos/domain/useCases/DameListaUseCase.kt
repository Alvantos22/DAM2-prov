package com.example.examen_mvvm_alvarosantos.domain.useCases


import com.example.examen_mvvm_alvarosantos.data.UsuarioRepository

class DameListaUseCase {
    fun dameLista()= UsuarioRepository.dameLista()
}