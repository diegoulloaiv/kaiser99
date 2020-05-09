package com.kaiser.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kaiser.R
import com.kaiser.logica.usuario
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_actividad_modificar_usuario.*

class actividad_modificar_usuario : AppCompatActivity() {

    lateinit var database: FirebaseFirestore
    lateinit var usuario_id: String
    lateinit var id_automatico : String
    private val RC_PHOTO_PICKER = 3
    var provincias: MutableList<String> = ArrayList<String>()
    var ciudades: MutableList<String> = ArrayList<String>()
    var aux_ciudad: String = ""
    var cambio_la_foto: Boolean = false
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var photourl: Uri
    var url_original: String = ""
    var usuario: usuario = usuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_modificar_usuario)

        usuario_id = intent.getStringExtra("id")
        buscar_info_usuario()
        btn_update_usuario_btn_imagen.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    RC_PHOTO_PICKER
            )
        }
        provincias = resources.getStringArray(R.array.provincias).toMutableList()
        ciudades = resources.getStringArray(R.array.ciudad_vacia).toMutableList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provincias)
        sp_update_usuario_provincia.adapter = adapter
        var adapterCiudad = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ciudades)
        sp_update_usuario_ciudad.adapter = adapterCiudad

        sp_update_usuario_provincia.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
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
                if (aux_ciudad.isNullOrEmpty())
                else {
                    sp_update_usuario_ciudad.setSelection(ciudades.indexOf(aux_ciudad))
                    aux_ciudad = ""
                }
            }


        }

        btn_update_usuario_actualizar.setOnClickListener()
        {
            if (cambio_la_foto == true) {
                firebaseStore = FirebaseStorage.getInstance()
                storageReference = firebaseStore!!.getReference().child("uploads")
                storageReference!!.putFile (photourl) .addOnFailureListener {
                    // failure
                } .addOnSuccessListener () {taskSnapshot ->
                    // success
                    storageReference!!.downloadUrl.addOnCompleteListener () { taskSnapshot ->

                        var url = taskSnapshot.result
                        usuario.photourl = url.toString()
                        actualizar_usuario()
                         }
                }

            } else {
                usuario.photourl = url_original
                actualizar_usuario()
            }



    }
}

    fun actualizar_usuario()
    {
        usuario.categoria = txt_update_usuario_categoria.text.toString()
        usuario.ciudad = sp_update_usuario_ciudad.selectedItem.toString()
        usuario.direccion = txt_update_usuario_direccion.text.toString()
        usuario.email = txt_update_usuario_email.text.toString()
        usuario.nombre = txt_update_usuario_nombre.text.toString()
        usuario.provincia = sp_update_usuario_provincia.selectedItem.toString()
        usuario.usuario_id = usuario_id

        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        database.collection("usuarios")
                .document(id_automatico)
                .set(usuario)
                .addOnSuccessListener { documentReference ->
                    this.finish()

                }
    }

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
        Picasso.with(this).load(data?.data).into(im_update_usuario_imagen)
        if (data != null) {
            photourl = data.data!!
            cambio_la_foto = true
        }
    }

}

fun buscar_info_usuario() {
    database = FirebaseFirestore.getInstance()
    database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    database
            .collection("usuarios")
            .whereEqualTo("usuario_id", usuario_id)
            .get()
            .addOnSuccessListener { items ->
                for (item in items) {
                    id_automatico = item.id
                    var aux = item.toObject(usuario::class.java)
                    txt_update_usuario_nombre.setText(aux.nombre)
                    txt_update_usuario_categoria.setText(aux.categoria)
                    txt_update_usuario_telefono.setText(aux.telefono)
                    txt_update_usuario_email.setText(aux.email)
                    url_original = aux.photourl
                    Glide.with(this /* context */)
                            .load(aux.photourl)
                            .into(im_update_usuario_imagen)
                    txt_update_usuario_direccion.setText(aux.direccion)
                    sp_update_usuario_provincia.setSelection(provincias.indexOf(aux.provincia))
                    sp_update_usuario_ciudad.setSelection(ciudades.indexOf(aux.ciudad))
                    aux_ciudad = aux.ciudad
                }
            }
}
}
