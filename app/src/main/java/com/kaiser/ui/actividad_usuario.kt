package com.kaiser.ui

//import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import androidx.core.util.Pair
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.kaiser.R
import com.kaiser.adaptadores.RecyclerAdapterPedidos
import com.kaiser.logica.pedidos
import com.kaiser.logica.producto
import com.kaiser.logica.tonos
import com.kaiser.logica.usuario
import kotlinx.android.synthetic.main.activity_actividad_usuario.*
import java.net.URI.create
import java.sql.Timestamp

class actividad_usuario : AppCompatActivity() {

    var lista_pedidos : MutableList<pedidos>? = ArrayList<pedidos>()
    private var usuario_id : String = ""
    lateinit var database: FirebaseFirestore
    private lateinit var adapter: RecyclerAdapterPedidos


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_usuario)
        usuario_id = intent.getStringExtra("id")
        buscar_info_usuario()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_pedidos .layoutManager = layoutManager
        adapter = RecyclerAdapterPedidos(lista_pedidos as ArrayList<pedidos>)
        rv_pedidos.adapter = adapter

        buscar_pedidos()
        btn_modificar_usuario.setOnClickListener()
        {
            val intent = Intent(this, actividad_modificar_usuario::class.java)
            intent.putExtra("id",usuario_id)
            val p1 = Pair.create<View, String>(im_usuario_foto, "imagen_perfil")
            val p2 = Pair.create<View, String>(txt_usuario_nombre,"nombre_usuario")
            val p3 = Pair.create<View,String>(txt_usuario_categoria,"categoria")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1,p2,p3)
            startActivity(intent,options.toBundle())
        }
    }

    fun buscar_pedidos()
    {
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        database
                .collection("pedidos")
                .whereEqualTo("usuario_id",usuario_id)
               // .orderBy("dateTime")
                .get()
                .addOnSuccessListener { items ->

                    for (item in items)
                    {
                        var aux = item.toObject(pedidos::class.java)
                        aux.id = item.id
                        lista_pedidos?.add(aux)
                        adapter.notifyItemInserted(lista_pedidos?.size!! -1)
                    }
                    //Toast.makeText(this,"a",Toast.LENGTH_SHORT)

                }
    }

    fun buscar_info_usuario()
    {
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        database
                .collection("usuarios")
                .whereEqualTo("usuario_id",usuario_id)
                .get()
                .addOnSuccessListener { items ->

                    for (item in items)
                    {
                        var aux = item.toObject(usuario::class.java)
                        txt_usuario_nombre.text = aux.nombre
                        txt_usuario_categoria.text = aux.categoria
                        Glide.with(this /* context */)
                                .load(aux.photourl)
                                .into(im_usuario_foto)
                    }
                }
    }
}
