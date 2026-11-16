package com.example.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.di.AppModule
import com.example.domain.model.Usuario
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            AppModule.guardarUseCase,
            AppModule.dameListaUseCase,
            AppModule.deleteUseCase,
            AppModule.dameUsuarioUseCase
        )
    }

    private lateinit var adapter: UsuarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        configRecycler()
        setupEventos()
        observarEstado()
    }

    private fun configRecycler() {
        binding.listado.layoutManager = LinearLayoutManager(this)

        adapter = UsuarioAdapter { usuario ->
            viewModel.handleDelete(usuario)
        }

        binding.listado.adapter = adapter
    }

    private fun setupEventos() {
        binding.editTextNombre.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    viewModel.handleNombre(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {}
            }
        )

        binding.buttonAdd.setOnClickListener {
            val nombre = binding.editTextNombre.text.toString().trim()

            if (nombre.isBlank()) {
                binding.editTextNombre.error = "Escribe un nombre"
            } else {
                viewModel.handleGuardar(
                    Usuario(
                        nombre = nombre,
                        apellido = "",
                        edad = 0
                    )
                )
            }
        }
    }

    private fun observarEstado() {
        viewModel.state.observe(this) { state ->

            adapter.submitList(state.usuarios)

            if (state.error != null) {
                Toast.makeText(
                    this,
                    state.error,
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.handleLimpiarError()
            }

            if (state.nombre.isEmpty() &&
                binding.editTextNombre.text.isNotEmpty()
            ) {
                binding.editTextNombre.text.clear()
            }
        }
    }
}