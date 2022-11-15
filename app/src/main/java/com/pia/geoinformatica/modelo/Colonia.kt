package com.pia.geoinformatica.modelo

 class Colonia {

     var id: String = ""
     var colonia: String = ""

     constructor(id: String, colonia: String) {
         this.id = id
         this.colonia = colonia
     }

     override fun toString(): String {
         return colonia
     }


 }