package com.kaiser.adaptadores

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.kaiser.R
import com.kaiser.logica.producto
import com.kaiser.ui.actividad_listado_productos
import com.kaiser.ui.actividad_producto


class RecyclerAdapterMenu(private var context: Context,private var dataList:ArrayList<String>):RecyclerView.Adapter<RecyclerAdapterMenu.ViewHolder>() {
    override fun getItemCount(): Int {
        return dataList.size;
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item_row_menu, parent, false))
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        //holder.textView.text=dataList.get(position);
        when (dataList.get(position)){
            "accesorios_estetica" -> holder.imagen.setImageResource(R.drawable.accesorios_estetica)
            "accesorios_pelu" -> holder.imagen.setImageResource(R.drawable.accesorios_pelu)
            "aparatologia" -> holder.imagen.setImageResource(R.drawable.aparatologia)
            "barberia" -> holder.imagen.setImageResource(R.drawable.barberia)
            "coloracion" -> holder.imagen.setImageResource(R.drawable.coloracion)
            "corpo" -> holder.imagen.setImageResource(R.drawable.corpo)
            "facial" -> holder.imagen.setImageResource(R.drawable.facial)
            "finalizacion" -> holder.imagen.setImageResource(R.drawable.finalizacion)
            "herramientas" -> holder.imagen.setImageResource(R.drawable.herramientas)
            "nutricion" -> holder.imagen.setImageResource(R.drawable.nutricion)
            "shampoo" -> holder.imagen.setImageResource(R.drawable.shampoo)
        }

        holder.imagen.adjustViewBounds = true
        holder.bindPhoto(dataList.get(position))
  /*     val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
      // Set the height by params
//        params.height = 170
        //params.width = 500
        // set height of RecyclerView
        holder.imagen.layoutParams = params
*/


    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!), View.OnClickListener {
        var imagen:ImageView = itemView!!.findViewById(R.id.itemImagenMenu)

        private var opcion : String? = null
        @SuppressLint("RestrictedApi")
        fun bindPhoto(o : String) {
            opcion = o
        }

        init {
            itemView?.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val context: Context = itemView.context
            val intent = Intent(context, actividad_listado_productos::class.java)
            intent.putExtra("opcion", opcion)
            context.startActivity(intent)
        }

        //var textView:TextView =itemView!!.findViewById(R.id.text_view)
    }



}