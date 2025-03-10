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
            //Abans de fer el registre validem les dades
            var email: String = correoEt.getText().toString()
            var pass: String = passEt.getText().toString()
            // validació del correu
            // si no es de tipus correu
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                correoEt.setError("Invalid Mail")
            } else if (pass.length < 6) {
                passEt.setError("Password less than 6 chars")
            } else {
                RegistrarJugador(email, pass)
            }

        }

    }

    fun RegistrarJugador(email:String, passw:String){
        auth.createUserWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        this,"createUserWithEmail:success",Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }
    fun updateUI(user: FirebaseUser?){
        //hi ha un interrogant perquè podria ser null
        if (user!=null)
        {
            var puntuacio: Int = 0
            var uidString: String = user.uid
            var correoString: String = correoEt.getText().toString()
            var passString: String = passEt.getText().toString()
            var nombreString: String = nombreEt.getText().toString()
            var fechaString: String= fechaTxt.getText().toString()
            //AQUI GUARDA EL CONTINGUT A LA BASE DE DADES
// FALTA FER
        }
        else
        {
            Toast.makeText( this,"ERROR CREATE USER",Toast.LENGTH_SHORT).show()
        }
    }
}
