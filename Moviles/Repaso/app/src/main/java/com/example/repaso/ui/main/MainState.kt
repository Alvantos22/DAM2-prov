package com.example.repaso.ui.main

import com.example.repaso.domain.model.Usuario

data class MainState (
    val usuarios:List<Usuario>,
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val error: String? = null,
)
