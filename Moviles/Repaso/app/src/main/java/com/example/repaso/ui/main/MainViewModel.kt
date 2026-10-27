package com.example.repaso.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.repaso.data.UsuarioRepository
import com.example.repaso.domain.model.Usuario
import com.example.repaso.domain.useCases.DameListaUseCase
import com.example.repaso.domain.useCases.DameUsuarioUseCase
import com.example.repaso.domain.useCases.DeleteUseCase
import com.example.repaso.domain.useCases.GuardarUseCase

class MainViewModel (
    private val guardarUseCase: GuardarUseCase,
    private val dameListaUseCase: DameListaUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val dameUsuarioUseCase: DameUsuarioUseCase
): ViewModel(){

    private val _state = MutableLiveData<MainState>(MainState(listOf(),"nombre","apellido",0,null))


    val state: LiveData<MainState> = _state
init {
    handleDameLista()
}

    fun handleGuardar(usuario: Usuario){
        try {
            guardarUseCase.guardar(usuario)
            _state.value = _state.value?.copy(usuarios = dameListaUseCase.dameLista())
        } catch (e: Exception){
            _state.value = _state.value?.copy(error = e.message)
        }
    }

    fun handleDameUsuario() {
        var usr = dameUsuarioUseCase.dameUsuario()
        _state.value = _state.value?.copy(nombre = usr.nombre, apellido = usr.apellido, edad = usr.edad)
    }

    fun handleDameLista() {
        _state.value = _state.value?.copy( usuarios = dameListaUseCase.dameLista())
    }

    fun handleDelete(usuario: Usuario) {
        try {
            deleteUseCase.delete(usuario)
            _state.value = _state.value?.copy(usuarios = dameListaUseCase.dameLista())
        }catch (e: Exception){
            _state.value = _state.value?.copy(error= e.message)
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
        if (modelClass.isAssignableFrom(com.example.repaso.ui.main.MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MainViewModel(
                guardarUseCase,
                dameListaUseCase,
                deleteUseCase,
                dameUsuarioUseCase

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}