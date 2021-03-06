package com.kaiser.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.kaiser.R
import com.kaiser.adaptadores.RecyclerAdapterListadoProductos
import com.kaiser.logica.producto
import kotlinx.android.synthetic.main.activity_actividad_listado_productos.*


class actividad_listado_productos : AppCompatActivity() {


    var lista_productos : MutableList<producto>? = ArrayList<producto>()
    private lateinit var adapter: RecyclerAdapterListadoProductos
    lateinit var database: FirebaseFirestore
    lateinit var categoria: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_listado_productos)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_listado_productos .layoutManager = layoutManager
        adapter = RecyclerAdapterListadoProductos(lista_productos as ArrayList<producto>)
        rv_listado_productos.adapter = adapter
        val decoration = DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        rv_listado_productos.addItemDecoration(decoration)
        categoria = intent.getStringExtra("opcion")
        when (categoria)
        {
            "coloracion" -> textView40.text = "Coloración"
            "shampoo" -> textView40.text = "Shampoo"
            "nutricion" -> textView40.text = "Nutrición"
            "finalizacion" -> textView40.text = "Finalización"
            "accesorios_pelu" -> textView40.text = "Accesorios de Peluquería"
            "herramientas" -> textView40.text = "Herramientas de Peluqueria"
            "barberia" -> textView40.text = "Productos de Barberia"
            "facial" -> textView40.text = "Productos Faciales"
            "corpo" -> textView40.text = "Productos Corporales"
            "accesorios_estetica" -> textView40.text = "Accesorios de Estetica"
            "aparatologia" -> textView40.text = "Aparatologia"
        }


        buscar_articulos()

    }

    private fun buscar_articulos()
    {
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        database
                .collection("productos")
                .whereEqualTo("categoria",categoria)
                .get()
                .addOnSuccessListener { items ->

                    for (item in items)
                    {
                        lista_productos?.add(item.toObject(producto::class.java))

                        val storageReference = FirebaseStorage.getInstance().reference.child("/productos" + item.id + ".jpg")
                        adapter.notifyItemInserted(lista_productos?.size!! -1)
                    }

                }
    }
}
