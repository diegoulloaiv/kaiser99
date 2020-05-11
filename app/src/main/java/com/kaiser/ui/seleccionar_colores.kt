package com.kaiser.ui

//import android.support.v7.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.kaiser.R
import com.kaiser.adaptadores.RecyclerAdapterListadoProductos
import com.kaiser.adaptadores.RecyclerAdapterTonos
import com.kaiser.logica.producto
import com.kaiser.logica.tonos
import kotlinx.android.synthetic.main.activity_actividad_listado_productos.*
import kotlinx.android.synthetic.main.activity_seleccionar_colores.*

class seleccionar_colores : AppCompatActivity() {

    lateinit var id : String
    //var lista_colores_seleccionados : MutableList<producto>? = ArrayList<producto>()
    var lista_colores : MutableList<tonos> = ArrayList<tonos>()
    lateinit var database: FirebaseFirestore
    private lateinit var adapter: RecyclerAdapterTonos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_colores)
        id = intent.getStringExtra("id")

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_listado_tonos .layoutManager = layoutManager
        adapter = RecyclerAdapterTonos(lista_colores as ArrayList<tonos>)
        rv_listado_tonos.adapter = adapter


        buscar_articulos()

        FAB_confirmar_tonos.setOnClickListener()
        {
            var cadena_resultado : String = ""
            for (a in lista_colores)
            {
                if (a.cantidad > 0)
                // ? separacion de item
                // * separacion de campos
                    cadena_resultado += a.numero + "*" + a.cantidad.toString() + "?"
            }
            val intent = Intent()
            intent.putExtra("array", cadena_resultado)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun buscar_articulos()
    {
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        database
                .collection("tonos")
                .whereEqualTo("tintura_id",id)
                .orderBy("orden")
                .get()
                .addOnSuccessListener { items ->

                    for (item in items)
                    {
                        lista_colores?.add(item.toObject(tonos::class.java))

                        //val storageReference = FirebaseStorage.getInstance().reference.child("/productos" + item.id + ".jpg")
                        adapter.notifyItemInserted(lista_colores?.size!! -1)
                    }

                }
    }
}