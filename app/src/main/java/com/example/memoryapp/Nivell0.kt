package com.example.memoryapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class Nivell0 : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private val cartas = mutableListOf<Carta>()
    private val cartasSeleccionadas = mutableListOf<Carta>()
    private lateinit var botones: MutableList<Button>
    private lateinit var textViewTiempo: TextView
    private lateinit var temporizador: CountDownTimer
    private var tiempoRestante: Long = 60000 // 60 segundos
    private var parejasEncontradas = 0
    private lateinit var textViewNivel: TextView  // Añadido


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nivell0)

        gridLayout = findViewById(R.id.gridLayout)
        textViewTiempo = findViewById(R.id.textViewTiempo)
        textViewNivel = findViewById(R.id.textViewNivel) // Inicialización


        inicializarCartas()
        mostrarCartas()

        Handler().postDelayed({
            cartas.forEach { it.esVisible = false }
            actualizarVista()
            iniciarTemporizador()
        }, 3000)
    }

    private fun inicializarCartas() {
        val valores = listOf("A", "B", "C", "D", "A", "B", "C", "D", "E", "F", "E", "F")
        valores.forEach { cartas.add(Carta(it)) }
        cartas.shuffle()
    }

    private fun mostrarCartas() {
        botones = mutableListOf()
        for (i in 0 until 12) {
            val carta = cartas[i]
            val button = Button(this).apply {
                text = "?"
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
            if (parejasEncontradas == 6) ganarJuego()
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
        for (i in 0 until 12) {
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
        temporizador.cancel()
        val intent = Intent(this, NivelCompletado::class.java)
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
