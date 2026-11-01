package com.example.primercomposito_asa.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.primercomposito_asa.ui.theme.PrimerComposito_AsaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrimerComposito_AsaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainPantalla(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PrimerComposito_AsaTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun MainPantallaPreview() {
    PrimerComposito_AsaTheme{
        MainPantalla()
    }
}

@Composable
fun MainPantalla(modifier: Modifier = Modifier) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            Text(
                text = "GESTOR DE RECURSOS",
                style =  MaterialTheme.typography.headlineMedium
            )

        }
    }
}