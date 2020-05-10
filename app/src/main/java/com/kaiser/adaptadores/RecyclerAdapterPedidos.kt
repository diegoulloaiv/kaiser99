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
import com.kaiser.ui.actividad_pedido
import kotlinx.android.synthetic.main.recycler_item_row_pedidos.view.*
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapterPedidos(private val pedidos: ArrayList<pedidos>) : RecyclerView.Adapter<RecyclerAdapterPedidos.PhotoHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterPedidos.PhotoHolder {
        val inflatedView = parent.inflate(R.layout.recycler_item_row_pedidos, false)
        return RecyclerAdapterPedidos.PhotoHolder(inflatedView)

    }

    override fun getItemCount(): Int {
        return pedidos.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val itemPhoto = pedidos[position]
        holder.bindPhoto(itemPhoto)
    }

    //1
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var pronos: pedidos? = null

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("RestrictedApi")
        fun bindPhoto(i: pedidos) {
            this.pronos = i
            val date = Date(i!!.fecha_hora.seconds * 1000L)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            sdf.timeZone = TimeZone.getTimeZone("GMT-4")
            val formattedDate = sdf.format(date)
            view.txt_pedido_fecha.text = formattedDate
            //view.txt_pedido_fecha.text = i!!.fecha_hora.toString()
            view.txt_pedido_estado.text = i!!.estado.toString()
            view.txt_pedido_total.text = i!!.total.toString()
            view.txt_pedido_metodo_envio.text = i!!.metodo_envio
            Toast.makeText(view.context,"a", Toast.LENGTH_SHORT)
        }


        //3
        init {
            v.setOnClickListener(this)

        }


            companion object {
            //5
            private val PHOTO_KEY = "PRODUCTO"
        }

        override fun onClick(p0: View?) {
            val context: Context = itemView.context
            val intent = Intent(context, actividad_pedido::class.java)
            intent.putExtra("id", pronos?.id)
            context.startActivity(intent)
        }
    }



}
