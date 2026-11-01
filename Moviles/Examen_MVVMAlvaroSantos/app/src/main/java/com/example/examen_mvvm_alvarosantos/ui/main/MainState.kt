package com.example.examen_mvvm_alvarosantos.ui.main

import com.example.examen_mvvm_alvarosantos.domain.model.Usuario

data class MainState (
    val usuarios:List<Usuario>,
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val email: String,
    val activo: Boolean,
    val error: String? = null,
)
