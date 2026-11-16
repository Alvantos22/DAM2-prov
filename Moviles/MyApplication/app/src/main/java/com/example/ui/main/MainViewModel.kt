package com.example.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.model.Usuario
import com.example.domain.useCases.DameListaUseCase
import com.example.domain.useCases.DameUsuarioUseCase
import com.example.domain.useCases.DeleteUseCase
import com.example.domain.useCases.GuardarUseCase

class MainViewModel(
    private val guardarUseCase: GuardarUseCase,
    private val dameListaUseCase: DameListaUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val dameUsuarioUseCase: DameUsuarioUseCase
) : ViewModel() {

    private val _state = MutableLiveData(MainState())

    val state: LiveData<MainState> = _state

    init {
        handleDameLista()
    }

    fun handleNombre(nombre: String) {
        _state.value = _state.value?.copy(nombre = nombre)
    }

    fun handleGuardar(usuario: Usuario) {
        try {
            guardarUseCase.guardar(usuario)

            _state.value = _state.value?.copy(
                usuarios = dameListaUseCase.dameLista(),
                nombre = "",
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value?.copy(
                error = e.message ?: "Error al añadir"
            )
        }
    }

    fun handleDameUsuario() {
        try {
            val usuario = dameUsuarioUseCase.dameUsuario()

            _state.value = _state.value?.copy(
                nombre = usuario.nombre,
                apellido = usuario.apellido,
                edad = usuario.edad,
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value?.copy(
                error = e.message ?: "Error al obtener usuario"
            )
        }
    }

    fun handleDameLista() {
        try {
            _state.value = _state.value?.copy(
                usuarios = dameListaUseCase.dameLista()
            )
        } catch (e: Exception) {
            _state.value = _state.value?.copy(
                error = e.message ?: "Error al obtener la lista"
            )
        }
    }

    fun handleDelete(usuario: Usuario) {
        try {
            deleteUseCase.delete(usuario)

            _state.value = _state.value?.copy(
                usuarios = dameListaUseCase.dameLista(),
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value?.copy(
                error = e.message ?: "Error al eliminar"
            )
        }
    }

    fun handleLimpiarError() {
        _state.value = _state.value?.copy(error = null)
    }
}

class MainViewModelFactory(
    private val guardarUseCase: GuardarUseCase,
    private val dameListaUseCase: DameListaUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val dameUsuarioUseCase: DameUsuarioUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                guardarUseCase,
                dameListaUseCase,
                deleteUseCase,
                dameUsuarioUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}