package com.example.examen_mvvm_alvarosantos.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.examen_mvvm_alvarosantos.domain.model.Usuario
import com.example.examen_mvvm_alvarosantos.domain.useCases.DameListaUseCase
import com.example.examen_mvvm_alvarosantos.domain.useCases.DeleteUseCase
import com.example.examen_mvvm_alvarosantos.domain.useCases.GuardarUseCase

class MainViewModel (
    private val guardarUseCase: GuardarUseCase,
    private val dameListaUseCase: DameListaUseCase,
    private val deleteUseCase: DeleteUseCase,
): ViewModel(){

    private val _state = MutableLiveData<MainState>(MainState(listOf(),"nombre","apellido",0,"nombre.apellido@example.es",false))


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
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.example.examen_mvvm_alvarosantos.ui.main.MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MainViewModel(
                guardarUseCase,
                dameListaUseCase,
                deleteUseCase,

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}