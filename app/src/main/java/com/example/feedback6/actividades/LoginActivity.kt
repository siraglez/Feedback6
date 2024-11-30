package com.example.feedback6.actividades

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.UsuarioDatabaseHelper
import com.example.feedback6.dataClasses.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var usuarioDbHelper: UsuarioDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usuarioDbHelper = UsuarioDatabaseHelper(this)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            GlobalScope.launch(Dispatchers.IO) {
                val esValido = usuarioDbHelper.verificarUsuario(email, password)
                withContext(Dispatchers.Main) {
                    if (esValido) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnRegister.setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val nuevoUsuario = Usuario(email = email, password = password)
                GlobalScope.launch(Dispatchers.IO) {
                    usuarioDbHelper.agregarUsuarioSiNoExiste(nuevoUsuario)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
