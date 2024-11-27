package com.example.feedback6.actividades

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.baseDeDatos.DatabaseProvider
import com.example.feedback6.dao.UsuarioDao
import com.example.feedback6.dataClasses.Usuario
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        usuarioDao = DatabaseProvider.getDatabase(this).usuarioDao()

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            GlobalScope.launch(Dispatchers.IO) {
                val usuario = usuarioDao.verificarUsuario(email, password)
                withContext(Dispatchers.Main) {
                    if (usuario != null) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val nuevoUsuario = Usuario(email = email, password = password)
                GlobalScope.launch(Dispatchers.IO) {
                    usuarioDao.agregarUsuario(nuevoUsuario)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}