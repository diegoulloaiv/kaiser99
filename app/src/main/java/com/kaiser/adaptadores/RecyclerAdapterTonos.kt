package com.kaiser.adaptadores

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.kaiser.R
import com.kaiser.logica.tonos

import kotlinx.android.synthetic.main.recyclerview_item_row_tonos.view.*

class RecyclerAdapterTonos(private val tonos: ArrayList<tonos>) : RecyclerView.Adapter<RecyclerAdapterTonos.PhotoHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterTonos.PhotoHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row_tonos, false)
        return RecyclerAdapterTonos.PhotoHolder(inflatedView)

    }

    override fun getItemCount(): Int {
        return tonos.size
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val itemPhoto = tonos[position]
        holder.bindPhoto(itemPhoto)
    }

    //1
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var tronos: tonos? = null

        @SuppressLint("RestrictedApi")
        fun bindPhoto(tono: tonos) {
            this.tronos = tono

            // Reference to an image file in Cloud Storage
            val storageReference = FirebaseStorage.getInstance().getReference("tonos")



            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(view.context /* context */)
                    .load(tronos!!.url)
                    .into(view.img_tono)

            view.txt_numero_tono.text = tronos!!.numero.toString()
            view.txt_nombre_tono.text = tronos!!.nombre.toString()
            view.txt_cantidad_tono.text = tronos!!.cantidad.toString()

            //Picasso.with(view.context).load(producto.id).into(view.itemImage)
            Toast.makeText(view.context,"a", Toast.LENGTH_SHORT)
        }


        //3
        init {
            v.btn_minus_tono.setOnClickListener()
            {
                var aux = v.txt_cantidad_tono.text.toString()
                v.txt_cantidad_tono.text  = (aux.toInt() + 1).toString()
                tronos?.cantidad  = v.txt_cantidad_tono.text.toString().toInt()
            }
            v.btn_minus_tono.setOnClickListener()
            {
                var aux = v.txt_cantidad_tono.text.toString()
                if (aux.toInt() > 0)
                {
                    v.txt_cantidad_tono.text  = (aux.toInt() - 1).toString()
                    tronos?.cantidad  = v.txt_cantidad_tono.text.toString().toInt()
                }
            }

        }


        companion object {
            //5
            private val PHOTO_KEY = "PRODUCTO"
        }

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }
    }



}