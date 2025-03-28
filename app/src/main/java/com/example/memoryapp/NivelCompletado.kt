package com.example.memoryapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button

class NivelCompletado : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nivel_completado)

        // Enlazar botones
        val buttonReintentar = findViewById<Button>(R.id.buttonReintentar)
        val buttonSeleccioNivells = findViewById<Button>(R.id.buttonSeleccioNivells)
        val buttonMenuPrincipal = findViewById<Button>(R.id.buttonMenuPrincipal)

        // Acción para "Tornar a intentar"
        buttonReintentar.setOnClickListener {
            // Aquí podrías reiniciar el nivel o reiniciar el juego, ejemplo:
            val intent = Intent(this, Nivell0::class.java) // Cambia a la actividad correspondiente
            startActivity(intent)
            finish() // Para cerrar la pantalla de NivelCompletado
        }

        // Acción para "Anar a selecció de nivells"
        buttonSeleccioNivells.setOnClickListener {
            val intent = Intent(this, MenuNivells::class.java) // Cambia a la actividad de selección de niveles
            startActivity(intent)
            finish() // Cerrar la pantalla actual
        }

        // Acción para "Menú principal"
        buttonMenuPrincipal.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) // Cambia a la actividad principal
            startActivity(intent)
            finish() // Cerrar la pantalla actual
        }
    }
}
