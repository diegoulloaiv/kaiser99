package com.kaiser.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.kaiser.R
import com.kaiser.logica.*
import kotlinx.android.synthetic.main.activity_actividad_producto.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class actividad_producto : AppCompatActivity() {

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f
    lateinit var txtTitulo: TextView
    lateinit var id: String
    lateinit var imagen: ImageView
    lateinit var precio: TextView
    lateinit var BtnAgregar: FloatingActionButton
    lateinit var cantidad: TextView
    var tiene_tonos: Boolean = false
    var lista_colores: MutableList<CarroEntity> = ArrayList<CarroEntity>()
    lateinit var lista_resultado: List<String>

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_producto)

        txtTitulo = this.findViewById(R.id.TxtTitulo)
        id = intent.getStringExtra("id")
        txt_texto_producto.movementMethod = ScrollingMovementMethod()
        txtTitulo.text = intent.getStringExtra("nombre")
        precio = this.findViewById(R.id.txt_precio)
        txt_texto_producto.text = Html.fromHtml(intent.getStringExtra("texto"), Html.FROM_HTML_MODE_COMPACT)
        txt_marca.text = intent.getStringExtra("marca")


        //CAMBiAR SEGUN EL USUARIO
        when (MyApplication.globalVar) {
            "publico" ->   precio.text =   intent.getStringExtra("precio1")
            "peluquero/a" -> precio.text = intent.getStringExtra("precio2")
            "cosmetologa/esteticista" -> precio.text = intent.getStringExtra("precio3")
            "maquillador/a" -> precio.text = intent.getStringExtra("precio4")
            "peluquera/maquilladora/cosmetologa" -> precio.text = intent.getStringExtra("precio5")
            "comercio" -> precio.text = intent.getStringExtra("precio6")
        }


        cantidad = this.findViewById(R.id.txtcantidad)

        tiene_tonos = intent.getStringExtra("tiene_tonos").toBoolean()
        imagen = this.findViewById(R.id.imageView)
        // Reference to an image file in Cloud Storage
        val storageReference = FirebaseStorage.getInstance().getReference("productos")
        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Glide.with(this /* context */)
                .load(intent.getStringExtra("url"))
                .into(this.imagen)

        if (tiene_tonos == true) {
            cantidad.text = "0"
            ib_plus.visibility = View.INVISIBLE
            ib_minus.visibility = View.INVISIBLE
            ib_paleta.visibility = View.VISIBLE
            txt_paleta.visibility = View.VISIBLE
            ib_paleta.setOnClickListener()
            {
                val intent = Intent(this, seleccionar_colores::class.java)
                intent.putExtra("id", id)
                startActivityForResult(intent, 1)
                //startActivity(intent)
            }
        } else {
            ib_paleta.visibility = View.INVISIBLE
            txt_paleta.visibility = View.INVISIBLE
            ib_plus.setOnClickListener()
            {
                cantidad.text = (cantidad.text.toString().toInt() + 1).toString()
            }

            ib_minus.setOnClickListener()
            {
                if (cantidad.text.toString().toInt() > 1)
                    cantidad.text = (cantidad.text.toString().toInt() - 1).toString()
            }
        }

        BtnAgregar = this.findViewById(R.id.FAB__producto)
        BtnAgregar.setOnClickListener()
        {
            val thread = Thread {
                if (tiene_tonos == true) {
                    for (carroEntity in lista_colores)
                    {
                        val db = Room.databaseBuilder(applicationContext, AppDb::class.java, "CarroDB")
                                .fallbackToDestructiveMigration()
                                .build()
                        db.carroDao().saveCarro(carroEntity)
                    }
                } else {
                    var carroEntity = CarroEntity()
                    carroEntity.productoNombre = txtTitulo.text.toString()
                    var aux: String = precio.text as String
                    carroEntity.productoPrecio = aux.toDouble()
                    var aux2: String = cantidad.text.toString()
                    carroEntity.productoCantidad = aux2.toInt()
                    val db = Room.databaseBuilder(applicationContext, AppDb::class.java, "CarroDB")
                            .fallbackToDestructiveMigration()
                            .build()
                    db.carroDao().saveCarro(carroEntity)
                }
            }
            thread.start()
            super.onBackPressed()
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    var cadena = data.getStringExtra("array")
                    lista_resultado = cadena.split("?")
                    for (item in lista_resultado) {
                        if (item.isNullOrEmpty()) {
                        } else {
                            var aux = item.split("*")
                            var entity: CarroEntity = CarroEntity()
                            entity.productoNombre = txtTitulo.text.toString() + " - " + aux[0].toString()
                            entity.productoCantidad = aux[1].toInt()
                            entity.productoPrecio = precio.text.toString().toDouble()
                            lista_colores.add(entity)
                            var aux_cantidad = cantidad.text.toString().toInt() + aux[1].toInt()
                            cantidad.text = aux_cantidad.toString()
                        }
                    }
                }
                Toast.makeText(this, "a", Toast.LENGTH_SHORT)
            }
        }
    }




}
