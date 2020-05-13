package com.kaiser.logica

import android.net.Uri
import java.io.Serializable

class usuario : Serializable {
    public var nombre : String = ""
    public var usuario_id : String = ""
    public var email : String = ""
    public var telefono : String = ""
    public var photourl : String  = ""
    public var direccion : String = ""
    public var ciudad : String = ""
    public var provincia: String = ""
    public var categoria: String = ""

    constructor(){}

    constructor(nombre : String, usuario_id: String, email: String, telefono: String, photourl: String, direcion: String, ciudad: String, provincia: String, categoria : String)
    {
        this.ciudad = ciudad
        this.direccion = direcion
        this.email = email
        this.nombre = nombre
        this.photourl = photourl
        this.provincia = provincia
        this.telefono = telefono
        this.usuario_id = usuario_id
        this.categoria = categoria
    }
}