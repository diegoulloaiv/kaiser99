package com.kaiser.adaptadores

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.kaiser.R
import com.kaiser.logica.MyApplication
import com.kaiser.logica.producto
import com.kaiser.ui.actividad_producto
import kotlinx.android.synthetic.main.activity_actividad_producto.view.*
import kotlinx.android.synthetic.main.recyclerview_item_row_listado_producto.view.*


class RecyclerAdapterListadoProductos(private val productos: ArrayList<producto>) : RecyclerView.Adapter<RecyclerAdapterListadoProductos.PhotoHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterListadoProductos.PhotoHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row_listado_producto, false)
        return RecyclerAdapterListadoProductos.PhotoHolder(inflatedView)

    }

    override fun getItemCount(): Int {
        return productos.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val itemPhoto = productos[position]
        holder.bindPhoto(itemPhoto)
    }

    //1
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var prducto: producto? = null

        @RequiresApi(Build.VERSION_CODES.N)
        @SuppressLint("RestrictedApi")
        fun bindPhoto(producto: producto) {
            this.prducto = producto
            // Reference to an image file in Cloud Storage
            val storageReference = FirebaseStorage.getInstance().getReference("productos")

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(view.context /* context */)
                    .load(prducto!!.url)
                    .into(view.iv_imagen_producto)

            view.txt_descripcion_producto.text = prducto!!.nombre.toString()
            //CAMBIAR CUANDO IMPLEMENTE LOS VARIOS PRECIOS
            var aux = "<strike> $" + producto.precio1.toString() + "</strike>"
            view.txt_precio_original.text = Html.fromHtml(aux, Html.FROM_HTML_MODE_COMPACT)
            when (MyApplication.globalVar) {
              "publico" ->   view.txt_precio_producto.text = producto!!.precio1.toString()
                "peluquero/a" -> view.txt_precio_producto.text = producto!!.precio2.toString()
                "cosmetologa/esteticista" -> view.txt_precio_producto.text = producto!!.precio3.toString()
                "maquillador/a" -> view.txt_precio_producto.text = producto!!.precio4.toString()
                "peluquera/maquilladora/cosmetologa" -> view.txt_precio_producto.text = producto!!.precio5.toString()
                "comercio" -> view.txt_precio_producto.text = producto!!.precio6.toString()
            }
            if (view.txt_precio_producto.text == producto.precio1.toString())
                view.txt_precio_original.visibility = View.INVISIBLE
            //Picasso.with(view.context).load(producto.id).into(view.itemImage)
            Toast.makeText(view.context,"a", Toast.LENGTH_SHORT)
        }


        //3
        init {
            v.setOnClickListener(this)
        }

        fun getActivity(context: Context?): Activity {
            return if (context is Activity) {
                context
            } else {
                getActivity((context as ContextWrapper).baseContext)
            }
        }

        //4
        override fun onClick(v: View) {
            val context: Context = v.context
            var actividad : Activity = getActivity(context)

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
            intent.putExtra("texto",prducto?.texto)
            intent.putExtra("marca",prducto?.nombre_proveedor)

            val imagen = Pair.create<View, String>(v.iv_imagen_producto, "imagen_producto")
            val nombre = Pair.create<View, String>(v.txt_descripcion_producto, "nombre_producto")
            val precio = Pair.create<View, String>(v.txt_precio_producto, "precio_producto")

            //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(actividad_listado_productos ,imagen)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(actividad,imagen,nombre,precio)
            context.startActivity(intent,options.toBundle())
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            //5
            private val PHOTO_KEY = "PRODUCTO"
        }
    }



}
