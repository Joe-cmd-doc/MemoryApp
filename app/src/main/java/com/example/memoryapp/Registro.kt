package com.example.memoryapp

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar

class Registro : AppCompatActivity() {

    private lateinit var correoEt: EditText
    private lateinit var passEt: EditText
    private lateinit var nombreEt: EditText
    private lateinit var fechaTxt: TextView
    private lateinit var Registrar: Button

    lateinit var auth: FirebaseAuth //FIREBASE AUTENTIFICACIO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializa las vistas
        correoEt = findViewById(R.id.correoEt)
        passEt = findViewById(R.id.passEt)
        nombreEt = findViewById(R.id.nombreEt)
        fechaTxt = findViewById(R.id.fechaTxt)
        Registrar = findViewById(R.id.Registrar)
        auth = FirebaseAuth.getInstance()

        // Cargar la fecha en el TextView
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = formatter.format(date)
        fechaTxt.text = formattedDate

        Registrar.setOnClickListener() {
            // Antes de registrar validamos las entradas
            var email: String = correoEt.text.toString()
            var pass: String = passEt.text.toString()

            // Validación del correo
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                correoEt.error = "Correo no válido"
            } else if (pass.length < 6) {
                passEt.error = "La contraseña debe tener al menos 6 caracteres"
            } else {
                RegistrarJugador(email, pass)
            }
        }
    }

    fun RegistrarJugador(email: String, passw: String) {
        auth.createUserWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso
                    Toast.makeText(this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Error al crear el usuario.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun updateUI(user: FirebaseUser?) {
        // Comprobamos si el usuario es no nulo
        if (user != null) {
            // Variables de datos
            val puntuacion = 0 // Esto lo puedes cambiar según tu lógica
            val uidString = user.uid
            val correoString = correoEt.text.toString()
            val passString = passEt.text.toString()
            val nombreString = nombreEt.text.toString()
            val fechaString = fechaTxt.text.toString()

            // Guardar los datos en un HashMap
            val dadesJugador: HashMap<String, String> = HashMap()
            dadesJugador["Uid"] = uidString
            dadesJugador["Email"] = correoString
            dadesJugador["Password"] = passString
            dadesJugador["Nom"] = nombreString
            dadesJugador["Data"] = fechaString
            dadesJugador["Puntuacio"] = puntuacion.toString()

            // Conectarse a Firebase Database
            val database = FirebaseDatabase.getInstance("https://memoryapp-7c04d-default-rtdb.firebaseio.com/")
            val reference: DatabaseReference = database.getReference("DATA_BASE_JUGADORS")

            // Guardar el usuario en la base de datos
            reference.child(uidString).setValue(dadesJugador).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Usuario guardado en la base de datos", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
        }
    }
}
