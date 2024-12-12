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

class LoginActivity : AppCompatActivity() {
    private lateinit var usuarioDbHelper: UsuarioDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usuarioDbHelper = UsuarioDatabaseHelper(this)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            // Validar credenciales
            if (usuarioDbHelper.verificarUsuario(email, password)) {
                // Inicio de sesión exitoso, redirigir a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cierra la actividad de inicio de sesión
            } else {
                // Mostrar un mensaje de error
                Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            // Crear un nuevo usuario y guardarlo en la base de datos
            if (email.isNotEmpty() && password.isNotEmpty()) {
                val nuevoUsuario = Usuario(email, password, false) // Por defecto, tema oscuro es false
                usuarioDbHelper.AgregarUsuario(nuevoUsuario)
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
