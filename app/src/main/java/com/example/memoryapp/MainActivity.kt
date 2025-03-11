package com.example.memoryapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //assigna valor a user
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        
        var BTMLOGIN = findViewById<Button>(R.id.BTMLOGIN);
        var BTMREGISTRO = findViewById<Button>(R.id.BTMREGISTRO);
        BTMLOGIN.setOnClickListener() {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        BTMREGISTRO.setOnClickListener() {

            val intent = Intent(this, Registro::class.java)
            startActivity(intent)

        }
    }

    // Aquest mètode s'executarà quan s'obri el menu
    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }
    private fun usuariLogejat() {
        if (user !=null)
        {
            val intent= Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }




    fun fesuntoast(view: View){
        Toast.makeText(this, "Has picat el botó!", Toast.LENGTH_SHORT).show()
        RegistrarJugador("pepe@pepe.com","123456")
    }

    fun RegistrarJugador (email:String,password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("pepe", "createUserWithEmail:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("pepe", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }

    }



}