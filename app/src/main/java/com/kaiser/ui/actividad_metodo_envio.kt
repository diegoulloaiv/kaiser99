package com.kaiser.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.kaiser.R
import com.kaiser.logica.usuario

import kotlinx.android.synthetic.main.activity_actividad_metodo_envio.*


class actividad_metodo_envio : AppCompatActivity() {

    private var usuario_string : String = ""
    private var locales = arrayListOf<String> ("Bahia Blanca", "Viedma", "Neuquén")
    private var provincia_seleccionada : String = ""
    private var ciudad_seleccionada  : String = ""
    private var local_seleccionado : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_metodo_envio)

        val adapterLocales = ArrayAdapter(this, R.layout.spinner_item, locales)
        sp_locales.adapter = adapterLocales

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            usuario_string = user.uid
        }

                sp_locales.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        local_seleccionado = locales[p2]
                        when (locales[p2])
                        {
                            "Bahia Blanca" -> txt_info_local.setText("Saavedra 164, Bahia Blanca \n (0291) - 4514602 ")
                            "Viedma" -> txt_info_local.setText("Buenos Aires 581, Viedma \n (02920) - 428204 ")
                            "Neuquén" -> txt_info_local.setText("Belgrano 2424, Neuquén \n (0299) - 5093730 ")
                         }
                    }

                }

        sp_provincia2.visibility = View.INVISIBLE
        sp_ciudad2.visibility = View.INVISIBLE
        txt_direccion2.visibility = View.INVISIBLE
        textView12.visibility = View.INVISIBLE
        textView11.visibility = View.INVISIBLE
        textView10.visibility = View.INVISIBLE
        textView23.visibility = View.INVISIBLE
        sp_locales.visibility = View.INVISIBLE
        txt_info_local.visibility = View.INVISIBLE

        ChipEnvio.setOnClickListener()
        {
            sp_provincia2.visibility = View.VISIBLE
            sp_ciudad2.visibility = View.VISIBLE
            txt_direccion2.visibility = View.VISIBLE
            textView12.visibility = View.VISIBLE
            textView11.visibility = View.VISIBLE
            textView10.visibility = View.VISIBLE
            textView23.visibility = View.INVISIBLE
            sp_locales.visibility = View.INVISIBLE
            txt_info_local.visibility = View.INVISIBLE
        }


        ChipLocal.setOnClickListener()
        {
            sp_provincia2.visibility = View.INVISIBLE
            sp_ciudad2.visibility = View.INVISIBLE
            txt_direccion2.visibility = View.INVISIBLE
            textView12.visibility = View.INVISIBLE
            textView11.visibility = View.INVISIBLE
            textView10.visibility = View.INVISIBLE
            textView23.visibility = View.VISIBLE
            sp_locales.visibility = View.VISIBLE
            txt_info_local.visibility = View.VISIBLE
        }

        var ciudades: MutableList<String>
        ciudades = resources.getStringArray(R.array.ciudad_vacia).toMutableList()
        val provincias = resources.getStringArray(R.array.provincias)
        val adapter = ArrayAdapter(this, R.layout.spinner_item, provincias)
        sp_provincia2.adapter = adapter

        btn_confirmar_pedido.setOnClickListener()
        {
            if (ChipEnvio.isChecked or ChipLocal.isChecked) {
                if (chipOnline.isChecked or ChipEfectivo.isChecked) {
                    val intent = Intent()
                    if (ChipEnvio.isChecked)
                        intent.putExtra("metodo_envio", "envio")
                    else
                        intent.putExtra("metodo_envio", "local")
                    if (chipOnline.isChecked)
                        intent.putExtra("metodo_pago", "online")
                    else
                        intent.putExtra("metodo_pago", "efectivo")
                    intent.putExtra("provincia", provincia_seleccionada)
                    intent.putExtra("ciudad", ciudad_seleccionada)
                    intent.putExtra("local", local_seleccionado)
                    var aux: String = txt_direccion2.text.toString()
                    intent.putExtra("direccion", aux)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                else
                    Toast.makeText(this,"Debe seleccionar una Forma de Pago", Toast.LENGTH_LONG)
            }
            else
                Toast.makeText(this,"Debe seleccionar un Metodo de Envio",Toast.LENGTH_LONG)
                //MercadoPago.SDK.configure("ENV_ACCESS_TOKEN")
        }

        var adapterCiudad = ArrayAdapter<String>(this, R.layout.spinner_item, ciudades)
        sp_ciudad2.adapter = adapterCiudad
        sp_provincia2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                provincia_seleccionada = provincias[position]
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

        sp_ciudad2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                ciudad_seleccionada = ciudades[p2]
            }

        }

        lateinit var database: FirebaseFirestore
        database = FirebaseFirestore.getInstance()
        var usuario_aux: usuario
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        database
                .collection("usuarios")
                .whereEqualTo("usuario_id", usuario_string)
                .get()
                .addOnSuccessListener { items ->

                    for (item in items) {
                        usuario_aux = item.toObject(usuario::class.java)
                        sp_provincia2.setSelection(provincias.indexOf(usuario_aux.provincia))
                        sp_ciudad2.setSelection(ciudades.indexOf(usuario_aux.ciudad))
                        txt_direccion2.setText(usuario_aux.direccion.toString())
                    }
                }
    }
}
