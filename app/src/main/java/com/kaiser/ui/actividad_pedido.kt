package com.kaiser.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.kaiser.R
import com.kaiser.adaptadores.RecyclerAdapterPedidosItems
import com.kaiser.logica.pedidos
import com.kaiser.logica.pedidos_Items
import kotlinx.android.synthetic.main.activity_actividad_pedido.*
import kotlinx.android.synthetic.main.activity_actividad_usuario.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.recycler_item_row_pedidos.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class actividad_pedido : AppCompatActivity() {

    var id : String = ""
    lateinit var database: FirebaseFirestore
    var listado_items : MutableList<pedidos_Items>? = ArrayList<pedidos_Items>()
    private lateinit var adapter: RecyclerAdapterPedidosItems

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_pedido)
        id = intent.getStringExtra("id")
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_pedidos_items .layoutManager = layoutManager
        adapter = RecyclerAdapterPedidosItems(listado_items as ArrayList<pedidos_Items>)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rv_pedidos_items.addItemDecoration(decoration)
        rv_pedidos_items.adapter = adapter
        buscar_info_pedido()

    }

    fun buscar_info_pedido()
    {
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        database
                .collection("pedidos")
                .document(id)
                .get()
                .addOnSuccessListener { item ->

                        var aux = item.toObject(pedidos::class.java)
                    if (aux != null) {
                        txt_detalle_pedido_estado.text= "Estado: ${aux.estado}"
                        txt_detalle_pedido_total.text  = "Total: $ ${aux.total.toString()}"
                        val date = Date(aux!!.fecha_hora.seconds * 1000L)
                        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                        sdf.timeZone = TimeZone.getTimeZone("GMT-4")
                        val formattedDate = sdf.format(date)
                        txt_detalle_pedido_fecha.text = "Fecha y Hora: $formattedDate"

                    }

                        buscar_items_pedido()
                }
    }

    fun buscar_items_pedido()
    {
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        database
                .collection("pedidos_items")
                .whereEqualTo("pedido_id",id)
                .get()
                .addOnSuccessListener { items ->
                    for (item in items)
                    {
                        listado_items?.add(item.toObject(pedidos_Items::class.java))
                        adapter.notifyItemInserted(listado_items?.size!! - 1)
                    }
                }
    }

}
