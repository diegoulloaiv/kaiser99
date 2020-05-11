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
import com.kaiser.logica.Carro
import com.kaiser.logica.producto
import com.kaiser.ui.actividad_carrito
import com.kaiser.ui.actividad_producto
import kotlinx.android.synthetic.main.recyclerview_item_row_carrito.view.*
import kotlinx.android.synthetic.main.recyclerview_item_row_listado_producto.view.*

class RecyclerAdapterCarrito(public val items: ArrayList<Carro>) : RecyclerView.Adapter<RecyclerAdapterCarrito.PhotoHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterCarrito.PhotoHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row_carrito , false)
        return RecyclerAdapterCarrito.PhotoHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val itemPhoto = items[position]
        holder.bindPhoto(itemPhoto)
    }

    private fun eliminar()
    {

    }

    override fun getItemCount(): Int {
        return items.size
    }

    //1
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var item: Carro? = null

        @SuppressLint("RestrictedApi")
        fun bindPhoto(i: Carro) {
            this.item = i

            // Reference to an image file in Cloud Storage
            /*val storageReference = FirebaseStorage.getInstance().getReference("productos")



            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(view.context /* context */)
                    .load(item!!.producto?.url!!)
                    .into(view.iv_producto_carro)
*/
            view.txt_producto_carro.text = item!!.producto?.nombre.toString()
            //CAMBIAR CUANDO IMPLEMENTE LOS VARIOS PRECIOS
            view.txt_precio_carro.text = "$ " + item!!.producto?.precio1.toString()
            view.txt_cantidad_carro.text = item!!.cantidad.toString()
            //Picasso.with(view.context).load(producto.id).into(view.itemImage)
            Toast.makeText(view.context,"a", Toast.LENGTH_SHORT)
        }

        init {

        }

        override fun onClick(p0: View?) {

        }


    }

}
