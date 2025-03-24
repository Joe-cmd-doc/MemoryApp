package com.example.memoryapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class Nivell0 : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private val cartas = mutableListOf<Carta>()
    private val cartasSeleccionadas = mutableListOf<Carta>()
    private lateinit var botones: MutableList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nivell0)

        gridLayout = findViewById(R.id.gridLayout)

        // Inicializamos las cartas
        inicializarCartas()

        // Mostrar las cartas en el GridLayout
        mostrarCartas()

        // Temporalmente mostrar todas las cartas por 3 segundos y luego esconderlas
        Handler().postDelayed({
            cartas.forEach { it.esVisible = false }
            actualizarVista()
        }, 3000)
    }

    // Inicializar las cartas con valores
    private fun inicializarCartas() {
        // Definimos los valores de las cartas
        val valores = listOf("A", "B", "C", "D", "A", "B", "C", "D", "E", "F", "E", "F")

        // Creamos los objetos Carta
        for (valor in valores) {
            cartas.add(Carta(valor))
        }

        // Mezclamos las cartas
        cartas.shuffle()
    }

    // Mostrar las cartas en el GridLayout
    private fun mostrarCartas() {
        botones = mutableListOf()

        for (i in 0 until 12) {
            val carta = cartas[i]
            val button = Button(this).apply {
                text = "?"
                setOnClickListener {
                    voltearCarta(carta, this)
                }
            }
            botones.add(button)
            gridLayout.addView(button)
        }
    }

    // Voltear una carta cuando el jugador hace clic
    private fun voltearCarta(carta: Carta, button: Button) {
        if (cartasSeleccionadas.size < 2 && !carta.esVisible) {
            carta.esVisible = true
            button.text = carta.valor
            cartasSeleccionadas.add(carta)

            // Si se han seleccionado dos cartas, comprobar si coinciden
            if (cartasSeleccionadas.size == 2) {
                verificarCoincidencia()
            }
        }
    }

    // Verificar si las dos cartas seleccionadas coinciden
    private fun verificarCoincidencia() {
        val carta1 = cartasSeleccionadas[0]
        val carta2 = cartasSeleccionadas[1]

        if (carta1.valor == carta2.valor) {
            // Las cartas coinciden, dejarlas visibles
        } else {
            // No coinciden, volver a voltear las cartas después de un segundo
            Handler().postDelayed({
                carta1.esVisible = false
                carta2.esVisible = false
                actualizarVista()
            }, 1000)
        }

        cartasSeleccionadas.clear()
    }

    // Actualizar la vista (recargar el GridLayout)
    private fun actualizarVista() {
        for (i in 0 until 12) {
            val carta = cartas[i]
            val button = botones[i]
            button.text = if (carta.esVisible) carta.valor else "?"
        }
    }

    // Modelo de carta
    data class Carta(
        val valor: String,
        var esVisible: Boolean = false
    )
}