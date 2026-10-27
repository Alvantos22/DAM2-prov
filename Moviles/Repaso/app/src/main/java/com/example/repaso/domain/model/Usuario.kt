package com.example.repaso.domain.model

data class Usuario(
    val nombre: String,
    val apellido: String,
    val edad: Int = 0
)