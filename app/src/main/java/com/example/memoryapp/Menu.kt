package com.example.memoryapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineStart
import java.io.ByteArrayInputStream
import java.io.InputStream
import android.util.Base64


class Menu : AppCompatActivity() {
    //creem unes variables per comprovar ususari i authentificació
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;
    lateinit var tancarSessio: Button
    lateinit var CreditsBtn: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button
    lateinit var editarBtn: Button
    private val IMAGE_PICK_CODE = 1000

    lateinit var miPuntuaciotxt: TextView
    lateinit var puntuacio: TextView
    lateinit var uid: TextView
    lateinit var correo: TextView
    lateinit var nom: TextView
    lateinit var edat: TextView
    lateinit var poblacio: TextView
    lateinit var imatgePerfil: ImageView
    lateinit var reference: DatabaseReference

    private var nivell ="1"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth= FirebaseAuth.getInstance()
        user =auth.currentUser

        tancarSessio =findViewById<Button>(R.id.tancarSessio)
        CreditsBtn =findViewById<Button>(R.id.CreditsBtn)
        PuntuacionsBtn =findViewById<Button>(R.id.PuntuacionsBtn)
        jugarBtn =findViewById<Button>(R.id.jugarBtn)
        editarBtn = findViewById<Button>(R.id.editarBtn)

        //Aquí creem un tipus de lletra a partir de una font
        val tf = Typeface.createFromAsset(assets,"fonts/comicate.TTF")
        miPuntuaciotxt=findViewById(R.id.miPuntuaciotxt)
        puntuacio=findViewById(R.id.puntuacio)
        uid=findViewById(R.id.uid)
        correo=findViewById(R.id.correo)
        nom=findViewById(R.id.nom)
        edat=findViewById(R.id.edat)
        poblacio=findViewById(R.id.poblacio)
        imatgePerfil=findViewById(R.id.imatgePerfil)

        //els hi assignem el tipus de lletra
        miPuntuaciotxt.setTypeface(tf)
        puntuacio.setTypeface(tf)
        uid.setTypeface(tf)
        correo.setTypeface(tf)
        nom.setTypeface(tf)
        editarBtn.setTypeface(tf)


        //fem el mateix amb el text dels botons
        tancarSessio.setTypeface(tf)
        CreditsBtn.setTypeface(tf)
        PuntuacionsBtn.setTypeface(tf)
        jugarBtn.setTypeface(tf)


        tancarSessio.setOnClickListener(){
            tancalaSessio()
        }
        CreditsBtn.setOnClickListener(){
            Toast.makeText(this,"Credits", Toast.LENGTH_SHORT).show()
        }
        PuntuacionsBtn.setOnClickListener(){
            Toast.makeText(this,"Puntuacions", Toast.LENGTH_SHORT).show()
        }
        jugarBtn.setOnClickListener(){
            //hem d'enviar el id, el nom i el contador, i el nivell
            var Uids : String = uid.getText().toString()
            var noms : String = nom.getText().toString()
            var puntuacios : String = puntuacio.getText().toString()
            var nivells : String = nivell
            val intent= Intent(this, MenuNivells::class.java)
            //val intent= Intent(this, Nivell0::class.java)
            intent.putExtra("UID",Uids)
            intent.putExtra("NOM",noms)
            intent.putExtra("PUNTUACIO",puntuacios)
            intent.putExtra("NIVELL",nivells)
            startActivity(intent)
            finish()

        }
        editarBtn.setOnClickListener {
            // Abrir el selector de imágenes cuando se haga clic en el botón de editar
            val selectImageButton = findViewById<Button>(R.id.editarBtn)

            selectImageButton.setOnClickListener {
                // Crear la intención para seleccionar una imagen de la galería
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, IMAGE_PICK_CODE)  // Este método maneja la respuesta

            }
        }



        consulta()

    }

    private fun Usuarilogejat()
    {
        if (user !=null)
        {
            Toast.makeText(this,"Jugador logejat",
                Toast.LENGTH_SHORT).show()
        }
        else
        {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    // Aquest mètode s'executarà quan s'obri el minijoc
    override fun onStart() {
        Usuarilogejat()
        super.onStart()
    }



    private fun tancalaSessio() {
        auth.signOut() //tanca la sessió
        //va a la pantalla inicial
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun consulta(){
        var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://memoryapp-7c04d-default-rtdb.firebaseio.com/")
        reference = database.getReference("DATA_BASE_JUGADORS")
        reference.addValueEventListener (object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i ("DEBUG","arrel value"+
                        snapshot.getValue().toString())
                Log.i ("DEBUG","arrel key"+ snapshot.key.toString())
                // ara capturem tots els fills
                var trobat:Boolean =false
                for (ds in snapshot.getChildren()) {
                    Log.i ("DEBUG","DS key:"
                        +ds.child("Uid").key.toString())
                                Log.i ("DEBUG","DS value:"
                        +ds.child("Uid").getValue().toString())
                                Log.i ("DEBUG","DS data:"
                        +ds.child("Data").getValue().toString())
                                Log.i ("DEBUG","DS mail:"
                        +ds.child("Email").getValue().toString())

                        if(ds.child("Email").getValue().toString().equals(user?.email)){

                            trobat=true

                            puntuacio.setText(
                                ds.child("Puntuacio").getValue().toString())
                            uid.setText(
                                ds.child("Uid").getValue().toString())
                            correo.setText(
                                ds.child("Email").getValue().toString())
                            nom.setText(
                                ds.child("Nom").getValue().toString())

                            nivell = ds.child("Nivell").getValue().toString()

                            poblacio.setText( ds.child("Poblacio").getValue().toString())
                            edat.setText( ds.child("Edat").getValue().toString())
                            var imatge: String = ds.child("Imatge").getValue().toString()
                            try {
                                if (imatge.isNotEmpty()) {
                                    // Si tenemos una cadena Base64 válida, decodificamos y mostramos la imagen
                                    val bitmap = base64ToBitmap(imatge)
                                    if (bitmap != null) {
                                        // Cargamos la imagen decodificada en el ImageView
                                        imatgePerfil.setImageBitmap(bitmap)
                                    } else {
                                        // Si la decodificación falla, usamos la imagen por defecto
                                        Picasso.get().load(R.drawable.hardlevel).into(imatgePerfil)
                                    }
                                } else {
                                    // Si la imagen está vacía, cargamos la imagen por defecto
                                    Picasso.get().load(R.drawable.hardlevel).into(imatgePerfil)
                                }
                            } catch (e: Exception) {
                                // Si ocurre algún error, cargamos la imagen por defecto
                                Picasso.get().load(R.drawable.hardlevel).into(imatgePerfil)
                            }
                        }
                        if (!trobat)
                        {
                            Log.e ("ERROR","ERROR NO TROBAT MAIL")
                        }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e ("ERROR","ERROR DATABASE CANCEL")


            }
            // Función para convertir una cadena Base64 a Bitmap
            fun base64ToBitmap(base64String: String): Bitmap? {
                return try {
                    // Decodificamos la cadena Base64 usando la versión correcta de Android
                    val decodedString = Base64.decode(base64String, Base64.DEFAULT)
                    val inputStream: InputStream = ByteArrayInputStream(decodedString)
                    BitmapFactory.decodeStream(inputStream) // Decodificamos a Bitmap
                } catch (e: Exception) {
                    e.printStackTrace() // Log del error
                    null // Si ocurre un error, retornamos null
                }
            }

        })
    }




}
