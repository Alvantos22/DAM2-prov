package com.example.examen_mvvm_alvarosantos.domain.model

data class Usuario(
    val nombre: String,
    val apellido: String,
    val curso: String,
    val edad: Int = 0,
    val email: String,
    val activo: Boolean,
)