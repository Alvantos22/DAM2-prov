package com.example.repaso.data

import com.example.repaso.domain.model.Usuario
import kotlin.random.Random

object UsuarioRepository {
    private val usuarios=mutableListOf(
        Usuario("Jakov","Rosales",85),
        Usuario("Alvaro","Santos",23),
        Usuario("Alejandra","Irigoyen",20)


    )

    fun dameUsuario()=usuarios[0]
    fun guardar(usuario: Usuario){
        if ( Random.nextDouble()> 0.60)
            throw RuntimeException("Error al añadir")
        usuarios.add(usuario)
    }
    fun dameLista()=usuarios.toList()
    fun delete(usuario: Usuario){
        usuarios.remove(usuario)
    }
}
