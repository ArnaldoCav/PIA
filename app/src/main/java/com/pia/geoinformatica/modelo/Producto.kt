package com.pia.geoinformatica.modelo

class Producto {

    var id: String = ""
    var descripcion: String = ""
    var precio: Double=0.0

    constructor(id: String, descripcion: String, precio:Double) {
        this.id = id
        this.descripcion = descripcion
        this.precio=precio
    }

    override fun toString(): String {
        return descripcion
    }
}