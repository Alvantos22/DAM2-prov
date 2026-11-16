package com.example.examen_mvvm_alvarosantos.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examen_mvvm_alvarosantos.databinding.ActivityMainBinding
import com.example.examen_mvvm_alvarosantos.di.AppModule
import com.example.examen_mvvm_alvarosantos.domain.model.Usuario


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            AppModule.guardarUseCase,
            AppModule.dameListaUseCase,
            AppModule.deleteUseCase,
        )
    }

    private lateinit var adapter: UsuarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.handleDameLista()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        configRecycler()
        observarEstado()
        setupEventos()
    }

    private fun configRecycler() {
        binding.listado.layoutManager = LinearLayoutManager(this)
        adapter = UsuarioAdapter({ usuario ->
            viewModel.handleDelete(usuario)
        })
        binding.listado.adapter = adapter
    }

    private fun setupEventos() {
            with(binding) {
                buttonAdd.setOnClickListener {
                    val u = Usuario(nombre = editTextNombre.text.toString(), apellido = "apellido", "2ºDAM", edad = 23, "nombre.apellido@example.com",false)
                    viewModel.handleGuardar(u)

                }
            }
    }

    private fun observarEstado() {
        viewModel.state.observe(this) { state ->
            state?.error?.let { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                viewModel.handleLimpiarError()
            }


            state?.usuarios.let{
                adapter.submitList(it)
            }



        }
    }
}