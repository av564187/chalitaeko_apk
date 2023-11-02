package com.example.charlitaeko

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = this.findViewById(R.id.imageView)
        val captureButton = this.findViewById<Button>(R.id.button2)

        captureButton.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(MainActivity.toString(), "Permiso de lectura de almacenamiento otorgado, abriendo galería...")
                openGallery()
            } else {
                Log.d(MainActivity.toString(), "Solicitando permiso de lectura de almacenamiento...")
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
            }
        }
        // Obtener una referencia a las preferencias compartidas
        val sharedPref = getSharedPreferences("suanfonsonPreferences", Context.MODE_PRIVATE)
// Editar las preferencias compartidas
        val editor = sharedPref.edit()
// Almacenar una variable (por ejemplo, un entero)
        var miVariable: String = "eko_pathtraversal-08b2c406-685e-11ee-8c99"
        editor.putString("token", miVariable)
// Aplicar los cambios
        editor.apply()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        @Suppress("DEPRECATION")
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            Log.d(MainActivity.toString(), "URI Image $selectedImageUri")
            if (selectedImageUri != null) {
                saveImageToInternalStorage(selectedImageUri)
            }
        }
    }

    private fun saveImageToInternalStorage(selectedImageUri: Uri) {
        val inputStream = contentResolver.openInputStream(selectedImageUri)
        if (inputStream != null) {
            val directory = filesDir
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "imagen_$timeStamp.jpg"
            val file = File(directory, fileName)

            try {
                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                inputStream.close()
                outputStream.flush()
                outputStream.close()
                Log.d(MainActivity.toString(), "Imagen guardada en almacenamiento interno.")
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(MainActivity.toString(), "Error al guardar la imagen en almacenamiento interno.")
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
        private const val REQUEST_PERMISSION = 2
    }
}