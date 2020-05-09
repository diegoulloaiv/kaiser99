package com.kaiser.adaptadores

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kaiser.R
import com.kaiser.logica.pedidos
import com.kaiser.logica.pedidos_Items
import com.kaiser.ui.actividad_pedido
import kotlinx.android.synthetic.main.recycler_item_row_pedidos.view.*
import kotlinx.android.synthetic.main.recyclerview_item_row_pedidos.view.*
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapterPedidosItems(private val items: ArrayList<pedidos_Items>) : RecyclerView.Adapter<RecyclerAdapterPedidosItems.PhotoHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row_pedidos, false)
        return RecyclerAdapterPedidosItems.PhotoHolder(inflatedView)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val itemPhoto = items[position]
        holder.bindPhoto(itemPhoto)
    }

    //1
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v){
        //2
        private var view: View = v
        private var itrems: pedidos_Items? = null

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("RestrictedApi")
        fun bindPhoto(i: pedidos_Items) {
            this.itrems = i
            view.txt_pedido_item_nombre.text = itrems!!.producto
            view.txt_pedido_item_cant.text = itrems!!.cantidad.toString()
            view.txt_pedido_item_importe.text = itrems!!.importe.toString()
            Toast.makeText(view.context,"a", Toast.LENGTH_SHORT)
        }



    }



}
