package com.example.charlitaeko

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import java.io.File
import java.io.IOException

class SuanfonsonProvider : ContentProvider() {

    companion object {
        const val ARCHIVOS = 1
        const val ARCHIVO_ID = 2
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI("com.example.charlitaeko.SuanfonsonProvider", "files", ARCHIVOS)
            uriMatcher.addURI("com.example.charlitaeko.SuanfonsonProvider", "files/#", ARCHIVO_ID)
        }
    }
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        return true
    }
//./adb shell content query --uri content://SuanfonsonProvider/files/../shared_prefs/
//./adb shell content query --uri content://SuanfonsonProvider/files/
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val context = context ?: return null
        val resultCursor: Cursor?
        Log.d("SuanfonsonProvider", "URI... $uri")
        val pathSegments = uri.pathSegments
        Log.d("SuanfonsonProvider", "segmentos... $pathSegments")

        //if (pathSegments.isNotEmpty()) {
            // El primer segmento de la URI podría indicar la ubicación de los archivos
            //val folderName = pathSegments[0]
            val folderName = uri.path
            Log.d("SuanfonsonProvider", "nombre del folderDir... $folderName")
            //val folderDir = File(context.applicationInfo.dataDir, folderName)
    // error sería confiar en como el dev define las rutas en las demás activities
            val folderDir = File(context.applicationInfo.dataDir,"/" + folderName)
            //val folderDir = File(context.filesDir, folderName)
            Log.d("SuanfonsonProvider", "nombre del folderDir... $folderDir")

            //if (folderDir.exists()) {
                val cursor = MatrixCursor(arrayOf("_id", "file_name", "file_content"))

                // Lista de archivos en la carpeta específica
                val files = folderDir.listFiles()
                Log.d("SuanfonsonProvider", "nombre del folder... ${files.toString()}")
                if (files != null) {
                    for ((index, file) in files.withIndex()) {
                        if (file.isFile()) {  // Asegurarse de que sea un archivo y no un directorio
                            val fileContent = readFileContent(file) // Función para leer el contenido del archivo
                            cursor.addRow(arrayOf(index, file.name, fileContent))
                        }
                    }

                    resultCursor = cursor
                } else {
                    Log.d("SuanfonsonProvider", "No se encontraron archivos en la carpeta.")
                    resultCursor = null
                }
            //} else {
            //    Log.d("SuanfonsonProvider", "Carpeta no encontrada: $folderDir")
            //    resultCursor = null
            //}
        //} else {
            // URI no contiene información sobre la ubicación de los archivos
        //    Log.d("SuanfonsonProvider", "URI no contiene información sobre la ubicación de los archivos")
        //    resultCursor = null
        //}

        return resultCursor
    }

    fun readFileContent(file: File): String {
        val content = StringBuilder()
        try {
            val bufferedReader = file.bufferedReader()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    content.append(it).append("\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content.toString()
    }


    /**        val context = context ?: return null
        val filesDir = context.filesDir
        Log.d(MainActivity.toString(), "URI... $filesDir")
        val resultCursor: Cursor?

        // Lista de archivos en la carpeta /files de la aplicación
        val files = filesDir.listFiles()

        // Crea un cursor con las columnas que desees mostrar en los resultados
        val cursor = MatrixCursor(arrayOf("_id", "file_name"))

        // Recorre la lista de archivos y agrega cada archivo al cursor
        for ((index, file) in files.withIndex()) {
            cursor.addRow(arrayOf(index, file.name))
        }

        // Asigna el cursor de resultados al cursor de retorno
        resultCursor = cursor

        return resultCursor **/

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}