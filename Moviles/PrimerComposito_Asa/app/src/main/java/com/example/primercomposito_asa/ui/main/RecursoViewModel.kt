//package com.example.primercomposito_asa.ui.main
//
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//
//
//class RecursoViewModel : ViewModel() {
//
//    var recursos by mutableStateOf<List<Recurso>>(emptyList())
//
//        private set
//    private var siguienteId=1
//
//
//    fun anadirRecurso(
//        nombre: String,
//        tipo: String,
//        cantidad: Int
//    ) {
//       val nuevoRecurso = Recurso(
//           id = siguienteId++,
//           nombre = nombre,
//           tipo = tipo,
//           cantidad = cantidad
//       )
//        recursos = recursos + nuevoRecurso
//    }
//    fun eliminarRecurso(id: Int) {
//        recursos = recursos.filter { it.id != id }
//    }
//    }
