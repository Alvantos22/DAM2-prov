package com.example.primercomposito_asa.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun MainPantalla(
    modifier: Modifier = Modifier,
    viewModel: RecursoViewModel = viewModel()
) {

    Scaffold(modifier = Modifier.fillMaxSize()){ innerPadding ->
        var nombre by rememberSaveable {
            mutableStateOf("")
        }

        var tipo by rememberSaveable {
            mutableStateOf("")
        }

        var cantidad by rememberSaveable {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            Text(
                text = "GESTOR DE RECURSOS",
                style =  MaterialTheme.typography.headlineMedium
            )

            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del recurso") }
            )

            TextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo de recurso") }
            )

            TextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") }
            )

            ElevatedButton(
                onClick = {
                    val numero = cantidad.toIntOrNull()

                    if (nombre.isNotBlank() &&
                        tipo.isNotBlank() &&
                        numero != null &&
                        numero > 0
                    ) {
                        viewModel.anadirRecurso(
                            nombre = nombre,
                            tipo = tipo,
                            cantidad = numero
                        )

                        nombre = ""
                        tipo = ""
                        cantidad = ""
                    }
                }
            ) {
                Text("Añadir recurso")
            }

            Text(
                text = "Recursos registrados: ${viewModel.contador}"
            )

            Column {
                viewModel.recursos.forEach { recurso ->

                    Text(
                        text = "${recurso.nombre} - ${recurso.tipo} - ${recurso.cantidad} unidades"
                    )

                }
            }

        }

    }
}