package com.example.mvvm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.domain.useCases.DamePresidenteUseCase
import kotlin.jvm.java

class MainViewModel(val damePresidenteUseCase: DamePresidenteUseCase) : ViewModel() {

private val _state= MutableLiveData<MainState>(MainState("inicio"))

    val state : LiveData<MainState> = _state

fun handleDamePresidente(){
var presi= damePresidenteUseCase.damePresidente()

_state.value=_state.value?.copy(nombrePresidente = presi.toString())?: MainState(presi.toString())
}

}


class MainViewModelFactory(private val damePresidenteUseCase: DamePresidenteUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.example.mvvm.ui.main.MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(damePresidenteUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

