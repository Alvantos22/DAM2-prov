package com.example.ui.main


import com.example.domain.model.Usuario

data class MainState(
    val usuarios: List<Usuario> = emptyList(),
    val nombre: String = "",
    val apellido: String = "",
    val edad: Int = 0,
    val error: String? = null
)