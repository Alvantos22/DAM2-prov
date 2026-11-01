package com.example.examen_mvvm_alvarosantos.data

import com.example.examen_mvvm_alvarosantos.domain.model.Usuario
import kotlin.random.Random

object UsuarioRepository {
    private val usuarios=mutableListOf(
        Usuario("Lucia","Alvarez Garcia","1ºDAM",18,"lucia.alvarez@iesquevedo.es",true),
        Usuario("Marco","Castillo Ruiz","2ºASIR",19,"marco.castillo@iesquevedo.es",true),
        Usuario("Sofia","Perez Martin","1ºDAM",1,"Sofia.perez@iesquevedo.es",false),



    )

    fun guardar(usuario: Usuario){
        usuarios.add(usuario)
    }
    fun dameLista()=usuarios.toList()
    fun delete(usuario: Usuario){
        usuarios.remove(usuario)
    }
}
