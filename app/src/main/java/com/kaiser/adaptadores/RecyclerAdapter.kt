package com.kaiser.adaptadores

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.kaiser.R
import com.kaiser.logica.producto
import com.kaiser.ui.actividad_producto
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class RecyclerAdapter(private val productos: ArrayList<producto>) : RecyclerView.Adapter<RecyclerAdapter.PhotoHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.PhotoHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row, false)
        return PhotoHolder(inflatedView)

    }

    override fun getItemCount(): Int {
        return productos.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.PhotoHolder, position: Int) {
        val itemPhoto = productos[position]
        holder.bindPhoto(itemPhoto)

    }

    //1
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var prducto: producto? = null

        @SuppressLint("RestrictedApi")
        fun bindPhoto(producto: producto) {
            this.prducto = producto

            // Reference to an image file in Cloud Storage
            val storageReference = FirebaseStorage.getInstance().getReference("productos")



            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(view.context /* context */)
                   .load(prducto!!.url)
                    .into(view.itemImage)


            //Picasso.with(view.context).load(producto.id).into(view.itemImage)
            Toast.makeText(view.context,"a",Toast.LENGTH_SHORT)
        }


        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            val context: Context = v.context
            val intent = Intent(context, actividad_producto::class.java)
            intent.putExtra("id", prducto?.id)
            intent.putExtra("nombre", prducto?.nombre)
            intent.putExtra("url", prducto?.url)
            intent.putExtra("proveedor", prducto?.producto_proveedor)
            intent.putExtra("precio1", prducto?.precio1.toString())
            intent.putExtra("precio2",prducto?.precio2.toString())
            intent.putExtra("precio3",prducto?.precio3.toString())
            intent.putExtra("precio4",prducto?.precio4.toString())
            intent.putExtra("precio5",prducto?.precio5.toString())
            intent.putExtra("precio6",prducto?.precio6.toString())
            intent.putExtra("text",prducto?.text)
            intent.putExtra("tiene_tonos",prducto?.tiene_tonos.toString())
            context.startActivity(intent)
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            //5
            private val PHOTO_KEY = "PRODUCTO"
        }
    }

}
