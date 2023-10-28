package com.example.charlitaeko

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SaveActivity : AppCompatActivity() {
    private val FILE_PICKER_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        val filePickerButton = this.findViewById<Button>(R.id.fileButton)
        filePickerButton.setOnClickListener {
            openFilePicker()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        @Suppress("DEPRECATION")
        startActivityForResult(intent, FILE_PICKER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("SaveActivity", "Intent ingresado... $data")
        if (requestCode == FILE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    Log.d("SaveActivity", "uri ingresada... $uri")
                    val inputStream = contentResolver.openInputStream(uri)

                    // Obtén el nombre del archivo original
                    //val fileName = uri.lastPathSegment ?: "temp_file"
                    val nameFile = uri.getQueryParameter("name")
                    Log.d("SaveActivity", "nombre del archivo con la nueva variable... $nameFile")
                    //Log.d("SaveActivity", "nombre del archivo... $fileName")


                    //val fileName = "archivo_$timeStamp.$originalFileExtension"
                    //Log.d("SaveActivity", "Nombre del Archivo creado... $fileName")
                    val file = File(applicationInfo.dataDir + "/cache", nameFile)
                    Log.d("SaveActivity", "Archivo creado... $file")
                    val outputStream = FileOutputStream(file)
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                    Toast.makeText(this, "Archivo guardado en ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al guardar el archivo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}



