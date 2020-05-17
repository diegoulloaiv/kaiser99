package com.kaiser.ui


import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.kaiser.R
import com.kaiser.logica.usuario
import com.kaiser.logica.variables_sistema
import kotlinx.android.synthetic.main.activity_actividad_metodo_envio.*

class actividad_metodo_envio : AppCompatActivity() {

    lateinit var database: FirebaseFirestore
    private var usuario_string: String = ""
    private var locales = arrayListOf<String>("Bahia Blanca", "Viedma", "Neuquén")
    private var provincia_seleccionada: String = ""
    private var ciudad_seleccionada: String = ""
    private var local_seleccionado: String = ""
    private var total: Double = 0.0
    private var cadete_sin_cargo: Double = 0.0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_metodo_envio)

        val adapterLocales = ArrayAdapter(this, R.layout.spinner_item, locales)
        sp_locales.adapter = adapterLocales
        total = intent.getDoubleExtra("total", 0.0)


        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            usuario_string = user.uid
        }

        sp_locales.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                local_seleccionado = locales[p2]
                when (locales[p2]) {
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
        txt_telefono_mp.visibility = View.INVISIBLE
        textView35.visibility = View.INVISIBLE

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

        ChipEfectivo.setOnClickListener()
        {
            txt_telefono_mp.visibility = View.INVISIBLE
            textView35.visibility = View.INVISIBLE
        }

        chipOnline.setOnClickListener()
        {
            txt_telefono_mp.visibility = View.VISIBLE
            textView35.visibility = View.VISIBLE
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


        btn_confirmar_pedido1.setOnClickListener()
        {
            if (ChipEnvio.isChecked or ChipLocal.isChecked) {
                if (chipOnline.isChecked or ChipEfectivo.isChecked) {

                    buscar_variables_sistema(ciudad_seleccionada)

                } else
                    Toast.makeText(this, "Debe seleccionar una Forma de Pago", Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(this, "Debe seleccionar un Metodo de Envio", Toast.LENGTH_LONG).show()

        }

        var adapterCiudad = ArrayAdapter<String>(this, R.layout.spinner_item, ciudades)
        sp_ciudad2.adapter = adapterCiudad
        sp_provincia2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

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
                        txt_telefono_mp.setText(usuario_aux.telefono)
                        sp_provincia2.setSelection(provincias.indexOf(usuario_aux.provincia))
                        sp_ciudad2.setSelection(ciudades.indexOf(usuario_aux.ciudad))
                        txt_direccion2.setText(usuario_aux.direccion.toString())
                    }
                }
    }

    fun buscar_variables_sistema(ciudad: String) {
        val rootRef = FirebaseFirestore.getInstance()
        val yourCollRef = rootRef.collection("variables_sistema")
        val query: Query = yourCollRef.whereEqualTo("variable", ciudad_seleccionada)
        query.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var entro = false
                for (document in task.result!!) {
                    var aux = document.toObject(variables_sistema::class.java)
                    entro = true
                    cadete_sin_cargo = aux.numero.toDouble()
                    if (total < cadete_sin_cargo) {
                        var faltante: Double = cadete_sin_cargo - total
                        AlertDialog.Builder(this@actividad_metodo_envio)
                                .setTitle("Envio sin Cargo")
                                .setMessage("Cuando el total es superior a $ ${cadete_sin_cargo} el envio es sin cargo. \n Te restan $ $faltante para que el envio sea sin cargo") // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Confirmar", DialogInterface.OnClickListener { dialog, which ->
                                    finalizar()
                                }) // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton("Volver", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show()

                    } else {
                        finalizar()
                        break
                            }
                    break
                }
                if (!entro) finalizar()
            }
        })


    }

    fun finalizar() {
        val intent = Intent()
        if (ChipEnvio.isChecked)
            intent.putExtra("metodo_envio", "envio")
        else
            intent.putExtra("metodo_envio", "local")
        if (chipOnline.isChecked)
        {
            intent.putExtra("telefono",txt_telefono_mp.text.toString())
            intent.putExtra("metodo_pago", "online")
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this,"a",Toast.LENGTH_LONG)
    }


}
