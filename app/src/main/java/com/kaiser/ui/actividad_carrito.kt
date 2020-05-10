package com.kaiser.ui


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.kaiser.R
import com.kaiser.adaptadores.RecyclerAdapterCarrito
import com.kaiser.adaptadores.RecyclerAdapterListadoProductos
import com.kaiser.logica.*
import kotlinx.android.synthetic.main.activity_actividad_carrito.*
import kotlinx.android.synthetic.main.activity_actividad_listado_productos.*
import kotlinx.coroutines.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import kotlin.coroutines.CoroutineContext

class actividad_carrito : AppCompatActivity() {

    var lista_carro: MutableList<Carro>? = ArrayList<Carro>()
    private lateinit var adapter: RecyclerAdapterCarrito
    private var total: Double = 0.0
    private var usuario_string: String = ""
    lateinit var p2: pedidos_Items

    //lateinit var sp_provincia: Spinner
    private var carro_vacio: Boolean = true
    //lateinit var sp_ciudad: Spinner

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            buscar_articulos()
        }

        //if (!carro_vacio) {

    }
    //}

    @RequiresApi(Build.VERSION_CODES.O)
    fun resto_main() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_carro.layoutManager = layoutManager
        adapter = RecyclerAdapterCarrito(lista_carro as ArrayList<Carro>)
        rv_carro.adapter = adapter
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            usuario_string = user.uid
        }




        btn_confirmar_pedido.setOnClickListener()
        {

            val intent = Intent(this, actividad_metodo_envio::class.java)
            intent.putExtra("total",total)
            startActivityForResult(intent, 2)


        }


    }

    fun insertar_pedidos_items(id_root: String) {
        var db = FirebaseFirestore.getInstance()
        for (items in this!!.lista_carro!!) {
            p2 = items.producto?.nombre?.let { pedidos_Items(id_root, it, items.producto!!.precio1, items.cantidad) }!!
            db.collection("pedidos_items")
                    .add(p2)
                    .addOnSuccessListener { documentReference ->

                    }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun buscar_articulos() {
        var aux_total: Double = 0.0
        var db = Room.databaseBuilder(applicationContext, AppDb::class.java, "CarroDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        db.carroDao().getAllCarro().forEach()
        {
            val aux: Carro = Carro()
            var aux_p: producto = producto()
            aux.cantidad = it.productoCantidad
            // USO PRECIO1, ESTE NO LO CAMBIO PORQUE AHI YA GUARDE EL CORRECTO//
            //PUTO EL QUE LEE//
            aux_p.nombre = it.productoNombre
            aux_p.precio1 = it.productoPrecio
            aux.producto = aux_p
            aux_total = aux_total + (aux.producto!!.precio1 * aux.cantidad)
            lista_carro?.add(aux)
//            adapter.notifyItemInserted(lista_carro?.size!! -1)
        }
        if (aux_total == 0.0) {
            carro_vacio = true
            setContentView(R.layout.carro_vacio)
        } else {
            carro_vacio = false
            setContentView(R.layout.activity_actividad_carrito)
            txt_carro_total.text = "Total: $ ${aux_total.toString()}"
            total = aux_total
            resto_main()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    var db = FirebaseFirestore.getInstance()
                    var ultimo_id = ""
                    val p1: pedidos = pedidos()
                    val id_pedido: String = ""
                    p1.metodo_envio = data.getStringExtra("metodo_envio")
                    p1.provincia = data.getStringExtra("provincia")
                    p1.ciudad = data.getStringExtra("ciudad")
                    p1.direccion = data.getStringExtra("direccion")
                    p1.metodo_pago = data.getStringExtra("metodo_pago")
                    p1.local = data.getStringExtra("local")
                    p1.fecha_hora = com.google.firebase.Timestamp.now()
                    p1.estado = "nuevo"
                    p1.total = total
                    p1.usuario = usuario_string
                    db.collection("pedidos")
                            .add(p1)
                            .addOnSuccessListener { documentReference ->
                                //                   super.onBackPressed()
                                ultimo_id = documentReference.id
                                insertar_pedidos_items(ultimo_id)
                                val t = Thread()
                                {
                                    var db = Room.databaseBuilder(applicationContext, AppDb::class.java, "CarroDB")
                                            .fallbackToDestructiveMigration().build()
                                    db.carroDao().nukeTable()

                                }
                                t.start()
                                super.onBackPressed()
                            }
                            .addOnFailureListener { e ->
                                //Log.w(TAG, "Error adding document", e)
                            }
                    // db.collection("pedidos").document().set(data)


                }
            }
        }
    }


}
