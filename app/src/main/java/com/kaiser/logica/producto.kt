package com.kaiser.logica

import java.math.BigDecimal
import java.util.*

class producto(var id: String,
                   var nombre: String,
                   var producto_proveedor: String,
                   var precio1: Double,
                   var precio2: Double,
                   var precio3: Double,
                   var precio4: Double,
                   var precio5: Double,
                   var precio6: Double,
                   var vendidos: Int,
                    var url: String,
                    var text : String,
                    var tiene_tonos : Boolean)
    {
        constructor() :this("",
                "",
                "",
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0,
                "",
                "",
                false
        )
    }