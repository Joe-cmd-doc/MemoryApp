package com.example.memoryapp

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



private var NOM: String =""
private var PUNTUACIO: String=""
private var UID: String=""
private var NIVELL: String=""




class MenuNivells : AppCompatActivity() {

    lateinit var imageButton1 :ImageButton
    lateinit var imageButton2 : ImageButton
    lateinit var imageButton3 :ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_nivells)
        //ara recuperarem els valors
        var intent:Bundle? = getIntent().extras
        UID = intent?.get("UID").toString()
        NOM = intent?.get("NOM").toString()
        PUNTUACIO = intent?.get("PUNTUACIO").toString()
        NIVELL = intent?.get("NIVELL").toString()
        //busco els 3 butons
        imageButton1 = findViewById(R.id.imageButton)
        imageButton2 = findViewById(R.id.imageButton2)
        imageButton3 = findViewById(R.id.imageButton3)
        //deshabilito els 3 buttons
        imageButton1.isEnabled=false
        imageButton2.isEnabled=false
        imageButton3.isEnabled=false
        imageButton1.visibility =View.GONE
        imageButton2.visibility =View.GONE
        imageButton3.visibility =View.GONE

        if (NIVELL =="1") {
            Toast.makeText(this,"NIVELL 1",Toast.LENGTH_LONG).show()
            imageButton1.isEnabled=true
            imageButton1.visibility =View.VISIBLE
        }
        if (NIVELL =="2")
        {
            Toast.makeText(this,"NIVELL 2",Toast.LENGTH_LONG).show()
            imageButton2.isEnabled=true
            imageButton2.visibility =View.VISIBLE
        }
        if (NIVELL =="3") {
            Toast.makeText(this,"NIVELL 3",Toast.LENGTH_LONG).show()
            imageButton3.isEnabled=true
            imageButton3.visibility = View.VISIBLE
    }
 }
}