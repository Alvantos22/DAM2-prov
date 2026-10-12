package com.example.mvvm.data

import com.example.mvvm.domain.model.Politico

object Politicos {
    private val politicos=mutableListOf(
        Politico("Albert Einstein", "Conservadores",0),
        Politico("Isaac Newton", "Ciudadanos",2),
        Politico("Pedro Sanchez","Perrsoe",248)
        )
    fun damePresidente()=politicos[1]
}

