package com.kaiser.logica

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.ServerValue
import java.io.Serializable
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class pedidos( var fecha_hora: com.google.firebase.Timestamp,
               var estado: String,
               var total: Double,
               var usuario: String,
               var items: MutableList<pedidos_Items>?,
                var id: String,
                var metodo_envio : String,
                var provincia : String,
                var ciudad : String,
                var direccion  : String,
                var metodo_pago : String,
                var local : String,
var observaciones : String,
var token : String)

{
     constructor() : this(
             com.google.firebase.Timestamp.now(),
            "",
            0.0,
            "",
            null,
             "",
             "",
             "",
             "",
             "",
             "",
             "",
             "",
             ""
    )
}

