package com.example.charlitaeko

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter (private val context: Context, private val fileList: Array<String>) :
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val fileName = fileList[position]
            // Cargar la imagen desde filesDir y mostrarla en la vista ImageView en el ViewHolder
            // Aquí necesitarás cargar la imagen usando Glide, Picasso u otra biblioteca de carga de imágenes.
            // Puedes pasar el nombre del archivo (fileName) y la ruta completa a la imagen para cargarla.
        }

        override fun getItemCount(): Int {
            return fileList.size
        }

        class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view)
        }
