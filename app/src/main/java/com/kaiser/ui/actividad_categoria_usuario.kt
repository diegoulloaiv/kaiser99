package com.kaiser.ui

//import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.kaiser.R
import com.kaiser.logica.MyApplication
import com.kaiser.logica.certificado
import kotlinx.android.synthetic.main.activity_actividad_categoria_usuario.*
import java.io.ByteArrayOutputStream
import java.io.File


class actividad_categoria_usuario : AppCompatActivity() {
    var listado_categoria = arrayListOf<String>("publico","peluquero/a","cosmetologa/esteticista","maquillador/a","peluquera/maquilladora/cosmetologa","comercio")
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentImagePath : File
    var photourl: Uri = Uri.EMPTY
    var categoria: Int = 0
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    var url_photo = ""
    lateinit var database: FirebaseFirestore
    var cargo_foto = false
    lateinit var mountainImagesRef : StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_categoria_usuario)

    val adapter = ArrayAdapter(this, R.layout.spinner_item_2, listado_categoria)
    sp_categoria_usuario.adapter = adapter
    txt_aviso_categoria1.visibility = View.INVISIBLE
    im_foto_certificado.visibility = View.INVISIBLE
    txt_aviso_categoria3.visibility = View.INVISIBLE
    pb_usuario_categoria.visibility = View.INVISIBLE

    im_foto_certificado.setOnClickListener()
    {
        val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(photoCaptureIntent,99)
    }

    btn_aceptar_categoria_usuario.setOnClickListener()
    {
        if (categoria > 0) {
            if (cargo_foto == false)
                Toast.makeText(this, "Se necesita Imagen de certificado", Toast.LENGTH_LONG).show()
            else {
                cargar_cargador()
                val storage = FirebaseStorage.getInstance()

                // [START upload_create_reference]
                // Create a storage reference from our app
                val storageRef = storage.reference

                // Create a reference to "mountains.jpg"
                var ruta = "certificados/" + MyApplication.usuario_id + ".jpg"
                val mountainsRef = storageRef.child(ruta)

                // Create a reference to 'images/mountains.jpg'
                var ruta2 = "certificados/" + ruta
                mountainImagesRef = storageRef.child(ruta2)
                // While the file names are the same, the references point to different files
                mountainsRef.name == mountainImagesRef.name // true
                mountainsRef.path == mountainImagesRef.path // false
                // Get the data from an ImageView as bytes
                im_foto_certificado.isDrawingCacheEnabled = true
                im_foto_certificado.buildDrawingCache()
                val bitmap = (im_foto_certificado.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                var uploadTask = mountainsRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener {task ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    var aux = mountainsRef.path + mountainsRef.name
                        insertar_categoria(aux)

                }

            }
        }
            else {
            cargar_cargador()
            insertar_categoria("")
        }

    }

    sp_categoria_usuario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            categoria = p2
            if (p2 == 0)
            {
                txt_aviso_categoria1.visibility = View.INVISIBLE
                im_foto_certificado.visibility = View.INVISIBLE
                txt_aviso_categoria3.visibility = View.INVISIBLE
            }
            else
            {
                txt_aviso_categoria1.visibility = View.VISIBLE
                im_foto_certificado.visibility = View.VISIBLE
                txt_aviso_categoria3.visibility = View.VISIBLE
            }
        }

    }
    }

    fun cargar_cargador()
    {
        pb_usuario_categoria.visibility = View.VISIBLE

        txt_aviso_categoria1.visibility = View.INVISIBLE
        im_foto_certificado.visibility = View.INVISIBLE
        txt_aviso_categoria3.visibility = View.INVISIBLE
        btn_aceptar_categoria_usuario.visibility = View.INVISIBLE
        sp_categoria_usuario.visibility = View.INVISIBLE
        textView34.visibility = View.INVISIBLE
    }

    fun insertar_categoria(ruta  : String) {
        val certificado = certificado(
        com.google.firebase.Timestamp.now(),
        MyApplication.usuario_id,
        ruta,
        listado_categoria[categoria],
        "nuevo"
        )
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        database.collection("solicitud_categoria")
                .add(certificado)
                .addOnSuccessListener { documentReference ->
                   // Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    this.finish()
                }
                .addOnFailureListener { e ->
                    //Log.w(TAG, "Error adding document", e)
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 99 && resultCode == RESULT_OK) {
            if (data != null) {
                val extras = data.extras
                val imageBitmap = extras!!["data"] as Bitmap?
                im_foto_certificado.setImageBitmap(imageBitmap)
                cargo_foto = true
                //mImageView.setImageBitmap(imageBitmap);
                //Picasso.with(this).load(data.data).into(im_foto_certificado)

            }
        }
    }




}
