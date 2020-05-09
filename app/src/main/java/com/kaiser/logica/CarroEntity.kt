package com.kaiser.logica

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CarroEntity {

    @PrimaryKey(autoGenerate = true) var id : Long = 0

    @ColumnInfo(name ="productoNombre")
    var productoNombre:  String =""

    @ColumnInfo(name = "productoPrecio")
    var productoPrecio : Double = 0.0

    @ColumnInfo(name = "prouctoCantidad")
    var productoCantidad : Int = 0
}