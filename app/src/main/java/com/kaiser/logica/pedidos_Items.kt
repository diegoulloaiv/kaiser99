package com.kaiser.logica

import java.io.Serializable

class pedidos_Items : Serializable{
      public var pedido_id : String = ""
      public var producto: String = ""
      public var importe : Double = 0.0
      public var cantidad : Int = 0

    constructor(){}

    constructor(pedido_id : String, producto: String, importe:Double, cantidad:Int)
    {
        this.cantidad = cantidad
        this.importe = importe
        this.pedido_id = pedido_id
        this.producto = producto
    }

}