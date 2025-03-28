package com.example.memoryapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class Nivell2 : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private val cartas = mutableListOf<Carta>()
    private val cartasSeleccionadas = mutableListOf<Carta>()
    private lateinit var botones: MutableList<Button>
    private lateinit var textViewTiempo: TextView
    private lateinit var temporizador: CountDownTimer
    private var tiempoRestante: Long = 75000 // 75s
    private var parejasEncontradas = 0
    private lateinit var textViewNivel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nivell2)

        gridLayout = findViewById(R.id.gridLayout)
        textViewTiempo = findViewById(R.id.textViewTiempo)
        textViewNivel = findViewById(R.id.textViewNivel)

        inicializarCartas()
        mostrarCartas()

        Handler().postDelayed({
            cartas.forEach { it.esVisible = false }
            actualizarVista()
            iniciarTemporizador()
        }, 5000)
    }

    private fun inicializarCartas() {
        val valores = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "A", "B", "C", "D", "E", "F", "G", "H")
        valores.forEach { cartas.add(Carta(it)) }
        cartas.shuffle()
    }

    private fun mostrarCartas() {
        botones = mutableListOf()
        for (i in 0 until 20) {
            val carta = cartas[i]
            val button = Button(this).apply {
                text = carta.valor
                setOnClickListener { voltearCarta(carta, this) }
            }
            botones.add(button)
            gridLayout.addView(button)
        }
    }

    private fun voltearCarta(carta: Carta, button: Button) {
        if (cartasSeleccionadas.size < 2 && !carta.esVisible) {
            carta.esVisible = true
            button.text = carta.valor
            cartasSeleccionadas.add(carta)
            if (cartasSeleccionadas.size == 2) verificarCoincidencia()
        }
    }

    private fun verificarCoincidencia() {
        val (carta1, carta2) = cartasSeleccionadas
        if (carta1.valor == carta2.valor) {
            parejasEncontradas++
            if (parejasEncontradas == 10) ganarJuego()
        } else {
            Handler().postDelayed({
                carta1.esVisible = false
                carta2.esVisible = false
                actualizarVista()
            }, 1000)
        }
        cartasSeleccionadas.clear()
    }

    private fun actualizarVista() {
        for (i in 0 until 20) {
            val carta = cartas[i]
            val button = botones[i]
            button.text = if (carta.esVisible) carta.valor else "?"
        }
    }

    private fun iniciarTemporizador() {
        temporizador = object : CountDownTimer(tiempoRestante, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tiempoRestante = millisUntilFinished
                textViewTiempo.text = "Temps: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                irAGameOver()
            }
        }.start()
    }

    private fun ganarJuego() {
        // Obtener el usuario actual
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val database = FirebaseDatabase.getInstance("https://memoryapp-7c04d-default-rtdb.firebaseio.com/")
            val reference = database.getReference("DATA_BASE_JUGADORS").child(uid)

            // Leer los datos del jugador
            reference.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val puntuacionActual = snapshot.child("Puntuacio").getValue(String::class.java)?.toDouble() ?: 0.0
                    val nivelActualEnBD = snapshot.child("Nivell").getValue(String::class.java)?.toInt() ?: 0
                    val nivelActualJugador = 0 // Este es el nivel actual del jugador, en este caso, 0 (puedes actualizar este valor según el nivel actual del jugador)

                    // Comprobar si el nivel en la base de datos es exactamente igual al nivel del jugador
                    if (nivelActualEnBD == nivelActualJugador) {
                        // Incrementar el nivel en 1
                        val nuevoNivel = nivelActualEnBD + 1

                        // Actualizar el nivel en la base de datos
                        reference.child("Nivell").setValue(nuevoNivel)

                        // Incrementar la puntuación en función del nivel
                        val incrementoPuntuacion = nivelActualJugador * 0.1 // Si nivel 0 => 0.0, si nivel 1 => 0.1, etc.
                        val nuevaPuntuacion = puntuacionActual + incrementoPuntuacion

                        // Actualizar la puntuación en la base de datos
                        reference.child("Puntuacio").setValue(nuevaPuntuacion).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Puntuación y nivel actualizados!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Error al actualizar la puntuación.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Si el nivel en la base de datos no coincide con el nivel actual, no se actualiza el nivel ni la puntuación
                        Toast.makeText(this, "El nivel en la base de datos no coincide, no se actualiza la puntuación.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error al obtener los datos del jugador.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "No se ha encontrado al usuario.", Toast.LENGTH_SHORT).show()
        }

        // Redirigir a la actividad de nivel completado
        val intent = Intent(this, NivelCompletado::class.java)
        intent.putExtra("nivellActual", 2) // Pasar el nivel actual
        startActivity(intent)
        finish()
    }

    private fun irAGameOver() {
        val intent = Intent(this, GameOver::class.java)
        startActivity(intent)
        finish()
    }

    data class Carta(val valor: String, var esVisible: Boolean = false)
}
