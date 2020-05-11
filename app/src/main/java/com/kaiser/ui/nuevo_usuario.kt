package com.kaiser.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kaiser.R
import com.kaiser.logica.usuario
import com.squareup.picasso.Picasso
// TODO: Arregar el nuevo usuario, seguro que anda como el culo. Y falta agregar la categoria


class nuevo_usuario : AppCompatActivity() {

    private val TAG = ""

    lateinit var nombre: String
    lateinit var email: String
    lateinit var id: String
    lateinit var telefono: String
    lateinit var photourl: Uri
    lateinit var direccion: String
    lateinit var txt_nombre: TextView
    lateinit var txt_telefono: TextView
    lateinit var txt_email: TextView
    lateinit var imagen_perfil: ImageView
    lateinit var btn_imagen_perfil: ImageButton
    lateinit var txt_direccion: TextView
    lateinit var btn_aceptar: Button
    lateinit var sp_provincia: Spinner
    lateinit var sp_ciudad: Spinner
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    lateinit var database: FirebaseFirestore
    private val RC_PHOTO_PICKER = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_n_usuario)
        nombre = intent.getStringExtra("nombre") ?: ""
        email = intent.getStringExtra("email") ?: ""
        id = intent.getStringExtra("id") ?: ""
        telefono = intent.getStringExtra("telefono") ?: ""
        photourl = Uri.parse(intent.getStringExtra("photourl") ?: "")

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = firebaseStore!!.getReference().child("uploads")

        txt_nombre = this.findViewById(R.id.txt_nombre)
        txt_telefono = this.findViewById(R.id.txt_telefono)
        txt_email = this.findViewById(R.id.txt_email)
        imagen_perfil = this.findViewById(R.id.imagen_perfil)
        btn_imagen_perfil = this.findViewById(R.id.btn_imagen_perfil)
        btn_imagen_perfil.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                RC_PHOTO_PICKER
            )
        }
        txt_direccion = this.findViewById(R.id.txt_direccion)

        sp_provincia = this.findViewById(R.id.sp_provincia)
        sp_ciudad = this.findViewById(R.id.sp_ciudad)
        var ciudades: MutableList<String>
        ciudades = resources.getStringArray(R.array.ciudad_vacia).toMutableList()
        val provincias = resources.getStringArray(R.array.provincias)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provincias)
        sp_provincia.adapter = adapter

        var adapterCiudad =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ciudades)
        sp_ciudad.adapter = adapterCiudad

        sp_provincia.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (provincias[position]) {
                    "Misiones" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_misiones).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "San Luis" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_san_luis).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Buenos Aires" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_buenos_aires).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Ciudad Autónoma de Buenos Aires" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_capital).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Chaco" -> {
                        ciudades = resources.getStringArray(R.array.ciudades_chaco).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Córdoba" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_cordoba).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Corrientes" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_corrientes).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Chubut" -> {
                        ciudades = resources.getStringArray(R.array.ciudades_chubus).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Entre Ríos" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_entre_rios).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Formosa" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_formosa).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Jujuy" -> {
                        ciudades = resources.getStringArray(R.array.ciudades_jujuy).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Mendoza" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_mendoza).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Neuquén" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_neuquen).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "La Rioja" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_la_rioja).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "La Pampa" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_la_pampa).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Río Negro" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_rio_negro).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Santa Cruz" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_santa_cruz).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "San Juan" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_san_juan).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Santa Fe" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_santa_fe).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Santiago del Estero" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_santiago).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Salta" -> {
                        ciudades = resources.getStringArray(R.array.ciudades_salta).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Tierra del Fuego" -> {
                        ciudades = resources.getStringArray(R.array.ciudades_tierra).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Tucumán" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_tucuman).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }

                    "Catamarca" -> {
                        ciudades =
                            resources.getStringArray(R.array.ciudades_catamarca).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }


                    else -> {
                        ciudades = resources.getStringArray(R.array.ciudad_vacia).toMutableList()
                        adapterCiudad.clear()
                        adapterCiudad.addAll(ciudades)
                    }
                }

            }


        }

        var direccionImagen: String = ""

        btn_aceptar = this.findViewById(R.id.btn_aceptar)
        btn_aceptar.setOnClickListener()
        {
            val ultima_parte = id
            val photoRef = ultima_parte?.let { it1 -> storageReference!!.child(it1) }
            photoRef?.putFile(photourl)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    direccionImagen = photoRef.downloadUrl.toString()
                    val vUsuario = usuario(
                        txt_nombre.text.toString(),
                        id,
                        txt_email.text.toString(),
                        txt_telefono.text.toString(),
                        direccionImagen,
                        txt_direccion.text.toString(),
                        sp_ciudad.selectedItem.toString(),
                        sp_provincia.selectedItem.toString(), ""
                    )// FALTA LA CATEGORIA
                    database = FirebaseFirestore.getInstance()
                    database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

                    database.collection("usuarios")
                        .add(vUsuario)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                            this.finish()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                }
            }


        }


        txt_nombre.text = nombre
        txt_telefono.text = telefono
        txt_email.text = email
        Picasso.with(this).load(photourl).into(imagen_perfil);


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            Picasso.with(this).load(data?.data).into(imagen_perfil)
            if (data != null) {
                photourl = data.data!!
            }
        }

    }


}
