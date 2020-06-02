package com.kaiser.logica

class certificado(
        var fecha_hora: com.google.firebase.Timestamp,
        var usuario_id : String,
        var certificado_url : String,
        var categoria : String,
        var estado : String,
        var token : String
) {
    constructor() : this (com.google.firebase.Timestamp.now(),
    "",
    "",
    "",
    "",
    "")
}