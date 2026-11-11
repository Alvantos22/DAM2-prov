package com.example.primercomposito_asa.ui.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.primercomposito_asa.ui.model.Recurso

class RecursoViewModel : ViewModel() {


    val recursos = mutableStateListOf<Recurso>()

        val contador: Int
        get() = recursos.size

    fun anadirRecurso(nombre: String, tipo: String, cantidad: Int) {

        val nuevoRecurso = Recurso(
            nombre = nombre,
            tipo = tipo,
            cantidad = cantidad
        )

        recursos.add(nuevoRecurso)
    }
}