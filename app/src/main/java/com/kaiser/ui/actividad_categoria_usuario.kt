package com.kaiser.ui

//import android.support.v7.app.AppCompatActivity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.kaiser.BuildConfig
import com.kaiser.R
import com.kaiser.logica.MyApplication
import com.kaiser.logica.certificado
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_actividad_categoria_usuario.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class actividad_categoria_usuario : AppCompatActivity() {

    var listado_categoria = arrayListOf<String>("publico", "peluquero/a", "cosmetologa/esteticista", "maquillador/a", "peluquera/maquilladora/cosmetologa", "comercio")


    var photourl: Uri = Uri.EMPTY
    var categoria: Int = 0
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = FirebaseStorage.getInstance().reference

    lateinit var database: FirebaseFirestore
    var cargo_foto = false
    lateinit var mountainImagesRef: StorageReference
    var REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String
    val REQUEST_TAKE_PHOTO = 1
    var file_name: String = ""

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

            /*val m_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg")
        val uri: Uri = FileProvider.getUriForFile(this, this.applicationContext.packageName + ".provider", file)
        m_intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(m_intent, 99)*/
            //val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //startActivityForResult(photoCaptureIntent,99)
            //askCameraPermissions();

            dispatchTakePictureIntent()

            //Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            //  takePictureIntent.resolveActivity(packageManager)?.also {
            //    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            //}
            //}

            //   dispatchTakePictureIntent()
            /*if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent()
        }
        else
        {
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),2)
        }*/
        }

        btn_aceptar_categoria_usuario.setOnClickListener()
        {
            if (categoria > 0) {
                if (cargo_foto == false)
                    Toast.makeText(this, "Se necesita Imagen de certificado", Toast.LENGTH_LONG).show()
                else {
                    cargar_cargador()
                    insertar_categoria(photourl.toString())
                }


            } else {
                cargar_cargador()
                insertar_categoria("")
            }

        }

        sp_categoria_usuario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                categoria = p2
                if (p2 == 0) {
                    txt_aviso_categoria1.visibility = View.INVISIBLE
                    im_foto_certificado.visibility = View.INVISIBLE
                    txt_aviso_categoria3.visibility = View.INVISIBLE
                } else {
                    txt_aviso_categoria1.visibility = View.VISIBLE
                    im_foto_certificado.visibility = View.VISIBLE
                    txt_aviso_categoria3.visibility = View.VISIBLE
                }
            }

        }
    }

    fun mostrar_ui()
    {
        pb_usuario_categoria.visibility = View.INVISIBLE
        txt_aviso_categoria1.visibility = View.VISIBLE
        im_foto_certificado.visibility = View.VISIBLE
        txt_aviso_categoria3.visibility = View.VISIBLE
        btn_aceptar_categoria_usuario.visibility = View.VISIBLE
        sp_categoria_usuario.visibility = View.VISIBLE
        textView34.visibility = View.VISIBLE
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
        "nuevo", MyApplication.token

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

    private fun uploadImageToFirebase(name: String, contentUri: Uri) {
        btn_aceptar_categoria_usuario.visibility = View.INVISIBLE
        val image = storageReference!!.child("certificados/$name")
        image.putFile(contentUri).addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot?> {
            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
                image.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri?> {
                    override fun onSuccess(uri: Uri?) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is $uri")
                        if (uri != null) {
                            photourl = uri
                            btn_aceptar_categoria_usuario.visibility = View.VISIBLE
                        }
                        //Picasso.get().load(uri).into(im_foto_certificado)
                    }
                })
                //Toast.makeText(this@actividad_categoria_usuario, "Image Is Uploaded.", Toast.LENGTH_SHORT).show()
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: Exception) {
                Toast.makeText(this@actividad_categoria_usuario, "Upload Failled.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = filesDir
        //val storageDir : File? = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        //val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            file_name = this.name
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //val imageBitmap = data?.extras?.get("data") as Bitmap
            im_foto_certificado.setImageURI(Uri.parse(currentPhotoPath))
            galleryAddPic()
            val uri2 : Uri = Uri.fromFile( File(currentPhotoPath))
            uploadImageToFirebase(file_name,uri2)
            cargo_foto = true
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.e("problema",ex.message)
                    null
                }
                Log.e("problema", "creo el archivo")
                // Continue only if the File was successfully created
                try {
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                                applicationContext,
                                BuildConfig.APPLICATION_ID + ".fileprovider",
                                it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
                catch (ex :Error )
                {
                    Log.e("problema2",ex.message)
                }
            }
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

}
