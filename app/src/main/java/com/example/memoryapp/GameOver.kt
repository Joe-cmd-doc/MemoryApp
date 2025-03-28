
package com.example.memoryapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class GameOver : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_over)

        val nivellActualInt = intent.getIntExtra("nivellActual", 0) // 0 es el valor por defecto si no se pasa nada
        // Enlazar botones
        val buttonReintentar = findViewById<Button>(R.id.buttonReintentar)
        val buttonSeleccioNivells = findViewById<Button>(R.id.buttonSeleccioNivells)
        val buttonMenuPrincipal = findViewById<Button>(R.id.buttonMenuPrincipal)

        // Acción para "Tornar a intentar"
        buttonReintentar.setOnClickListener {
            // Usar un when para decidir qué actividad lanzar según el nivel
            val intent = when (nivellActualInt) {
                0 -> Intent(this, Nivell0::class.java)
                1 -> Intent(this, Nivell1::class.java)
                2 -> Intent(this, Nivell2::class.java)
                3 -> Intent(this, Nivell3::class.java)
                else -> Intent(this, Nivell0::class.java) // Valor por defecto si no se pasa un nivel válido
            }
            startActivity(intent)
            finish() // Para cerrar la pantalla de GameOver
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
