package com.kaiser.logica

import java.io.Serializable

class variables_sistema : Serializable {
       var variable: String = ""
       var cadena: String = ""
       var numero: Int = 0

    constructor(){}

    constructor(variable : String, cadena : String, numero : Int)
    {
        this.variable = variable
        this.cadena = cadena
        this.numero = numero
    }

}