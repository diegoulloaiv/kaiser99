package com.kaiser.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.kaiser.R
import com.kaiser.adaptadores.RecyclerAdapter
import com.kaiser.adaptadores.RecyclerAdapterMenu
import com.kaiser.logica.AppDb
import com.kaiser.logica.MyApplication
import com.kaiser.logica.producto
import com.kaiser.logica.usuario
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.rv_mas_vendidos
import kotlinx.android.synthetic.main.activity_main.rv_menu
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class   MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    lateinit var database: FirebaseFirestore
    var lista_productos_vendidos : MutableList<producto>? = ArrayList<producto>()
    var lista_menu : MutableList<String>? = ArrayList<String>()
    private lateinit var adapter: RecyclerAdapter
    private lateinit var adaptador_menu : RecyclerAdapterMenu
    private lateinit var usuario_id : String

    lateinit var btnaux : Button
    lateinit var txtaux : TextView
    lateinit var btnaux2 : Button

    lateinit var carrito : ImageView

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job



    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //Logout()

        im_usuario.setOnClickListener()
        {
            val intent = Intent(this, actividad_usuario::class.java)
            intent.putExtra("id",usuario_id)
            startActivity(intent)
        }
        //linearLayoutManager = LinearLayoutManager(this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_mas_vendidos.layoutManager = layoutManager
        adapter = RecyclerAdapter(lista_productos_vendidos as ArrayList<producto>)
        rv_mas_vendidos.adapter = adapter

        // IF PELUQUERO
        //        {
        lista_menu?.add("coloracion")
        lista_menu?.add("shampoo")
        lista_menu?.add("nutricion")
        lista_menu?.add("finalizacion")
        lista_menu?.add("accesorios_pelu")
        lista_menu?.add("herramientas")
        lista_menu?.add("barberia")
        lista_menu?.add("facial")
        lista_menu?.add("corpo")
        lista_menu?.add("accesorios_estetica")
        lista_menu?.add("aparatologia")

        //}
        val layoutManager_menu = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_menu.layoutManager = layoutManager_menu
        adaptador_menu = RecyclerAdapterMenu(this, lista_menu as ArrayList<String>)
        rv_menu.adapter = adaptador_menu

        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            var user = firebaseAuth.currentUser
            if (user != null) {
                cargar_informacion_usuario()
            } else {
                val providers = arrayListOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build()
                )

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setLogo(R.mipmap.logo)
                                .setTheme(R.style.MyTheme)
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN
                )

            }
        }
        buscar_articulos_mas_vendidos()

        job = Job() // create the Job
/*
        txtaux = findViewById(R.id.txtaux)
        btnaux = findViewById(R.id.btnaux)
        btnaux.setOnClickListener()
        {
            launch {
                val resultado = async(Dispatchers.IO) { buscar_db() }
                actualizar_ui(resultado.await())
            }
        }

        btnaux2 = findViewById(R.id.button2)
        btnaux2.setOnClickListener()
        {
            val t = Thread()
            {
                var db = Room.databaseBuilder(applicationContext, AppDb::class.java, "CarroDB")
                        .fallbackToDestructiveMigration().build()
                db.carroDao().nukeTable()
            }
            t.start()
        }

      */

        carrito = im_carrito
        carrito.setOnClickListener()
        {
            val intent = Intent(this, actividad_carrito::class.java)
            startActivity(intent)
        }
    }

    fun actualizar_ui(texto: String)
    {
        txtaux.text = texto
    }

    fun cargar_informacion_usuario()
    {
        // CHEQUEO SI EL USUARIO SE LOGUEO CORRECTAMENTE
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
           usuario_id = user.uid
            val uid = user.uid
            val nombre = user.displayName
            val email = user.email
            val telefono = user.phoneNumber
            val photourl = user.photoUrl
            checkIfUserExists(uid, nombre, email, telefono, photourl)
        }
    }

    fun buscar_db(): String
    {
        var aux : String = ""
            var db = Room.databaseBuilder(applicationContext, AppDb::class.java, "CarroDB")
                    .fallbackToDestructiveMigration() .build()
            db.carroDao().getAllCarro().forEach()
            {
                aux = aux + "producto: " + it.productoNombre + " Precio: " + it.productoPrecio.toString() + "\n"
            }
        return aux
    }


    fun buscar_articulos_mas_vendidos()
    {
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        database
                .collection("productos")
                .orderBy("vendidos", Query.Direction.DESCENDING).limit(3)
                .get()
                .addOnSuccessListener { items ->

                    for (item in items)
                    {
                        lista_productos_vendidos?.add(item.toObject(producto::class.java))

                        val storageReference = FirebaseStorage.getInstance().reference.child("/productos" + item.id + ".jpg")
                        adapter.notifyItemInserted(lista_productos_vendidos?.size!! -1)
                    }
                    //for (producto in this!!.lista_productos_vendidos!!)
                      //  Toast.makeText(this,producto.nombre, Toast.LENGTH_SHORT)
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                cargar_informacion_usuario()
            }
        else if (resultCode == Activity.RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }

    public fun Logout() {
        AuthUI.getInstance().signOut(this)
        finish()
    }

    // Tests to see if /users/<userId> has any data.
    @SuppressLint("RestrictedApi")
    fun checkIfUserExists(userId: String,nombre : String? ,email : String? ,telefono : String? ,photourl : Uri? ) {
        var existia = false
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        val intent = Intent(this, nuevo_usuario::class.java)
        intent.putExtra("id",userId)
        intent.putExtra("nombre", nombre)
        intent.putExtra("email", email)
        intent.putExtra("telefono", telefono)
        intent.putExtra("photourl", photourl.toString())

        database
                .collection("usuarios")
                .whereEqualTo("usuario_id", userId)
                .get()
                .addOnSuccessListener { documents ->
                    try {
                        if (documents != null) {
                            for (document in documents) {
                                var aux_usuario = document.toObject(usuario::class.java)
                                Log.d(TAG, "Usuario Existia ${document.id} => ${document.data}")
                                MyApplication.globalVar = aux_usuario.categoria
                                MyApplication.usuario_id = document.id
                                existia = true
                            }
                            //Toast.makeText(
                            //    this,
                            //   "DocumentSnapshot read successfully!",
                            //   Toast.LENGTH_LONG
                            //).show()
                            if (existia == false) {
                                startActivity(intent)
                            }
                        } else {
                            //Toast.makeText(this, "No such document!", Toast.LENGTH_LONG).show()
                            if (existia == false) {
                                val intent = Intent(this,
                                    nuevo_usuario::class.java)
                                intent.putExtra("id",userId)
                                startActivity(intent)
                            }
                        }
                    } catch (ex: Exception) {
                        //Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                        //Log.e(TAG, ex.message)
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Error writing document", Toast.LENGTH_LONG).show()
                }

    }

    override fun onDestroy() {
        job.cancel() // cancel the Job
        super.onDestroy()
    }

}


