package com.example.memoryapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Registro : AppCompatActivity() {

    private lateinit var correoEt: EditText
    private lateinit var passEt: EditText
    private lateinit var nombreEt: EditText
    private lateinit var fechaTxt: TextView
    private lateinit var registrarBtn: Button
    private lateinit var edatEt: EditText
    private lateinit var poblacioEt: EditText
    private lateinit var selectImageBtn: Button
    private lateinit var imageView: ImageView

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var selectedBitmap: Bitmap? = null

    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicialitzar Firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://memoryapp-7c04d-default-rtdb.firebaseio.com/")
            .getReference("DATA_BASE_JUGADORS")

        // Vinculació de vistes
        correoEt = findViewById(R.id.correoEt)
        passEt = findViewById(R.id.passEt)
        nombreEt = findViewById(R.id.nombreEt)
        fechaTxt = findViewById(R.id.fechaTxt)
        registrarBtn = findViewById(R.id.Registrar)
        edatEt = findViewById(R.id.edatEt)
        poblacioEt = findViewById(R.id.poblacioEt)



        // Mostrar la data actual
        val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        fechaTxt.text = formattedDate

        // Seleccionar imatge
        selectImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        // Registre d'usuari
        registrarBtn.setOnClickListener {
            val email = correoEt.text.toString()
            val pass = passEt.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                correoEt.error = "Correu no vàlid"
            } else if (pass.length < 6) {
                passEt.error = "La contrasenya ha de tenir almenys 6 caràcters"
            } else {
                registrarJugador(email, pass)
            }
        }
    }

    private fun registrarJugador(email: String, passw: String) {
        auth.createUserWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Usuari creat amb èxit", Toast.LENGTH_SHORT).show()
                    updateUI(user)
                } else {
                    Toast.makeText(this, "Error en crear l'usuari", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val uidString = user.uid
            val dadesJugador = HashMap<String, String>()

            dadesJugador["Uid"] = uidString
            dadesJugador["Email"] = correoEt.text.toString()
            dadesJugador["Password"] = passEt.text.toString()
            dadesJugador["Nom"] = nombreEt.text.toString()
            dadesJugador["Data"] = fechaTxt.text.toString()
            dadesJugador["Edat"] = edatEt.text.toString()
            dadesJugador["Poblacio"] = poblacioEt.text.toString()
            dadesJugador["Puntuacio"] = "0"
            dadesJugador["Nivell"] = "1"

            // Solo convertir la imagen por defecto a Base64
            val defaultImage = BitmapFactory.decodeResource(resources, R.drawable.fotoy) // Usamos la imagen predeterminada
            val defaultImageBase64 = encodeImageToBase64(defaultImage)
            dadesJugador["Imatge"] = defaultImageBase64



            // Guardar dades a Firebase
            databaseReference.child(uidString).setValue(dadesJugador).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Usuari guardat a la base de dades", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al guardar usuari", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                val inputStream = contentResolver.openInputStream(it)
                selectedBitmap = BitmapFactory.decodeStream(inputStream)
                imageView.setImageBitmap(selectedBitmap)
            }
        }
    }
}
