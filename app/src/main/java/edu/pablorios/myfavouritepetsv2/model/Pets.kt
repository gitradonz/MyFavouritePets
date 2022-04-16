package edu.pablorios.myfavouritepetsv2.model

import java.io.Serializable

// Clase con la asignacion de cada mascota
class Pets(): Serializable{

    // las variables con los datos que tendrán nuestras mascotas
    var id: Int? = null
    var nom: String? = null
    var nomCie: String? = null
    var pel: String? = null
    var clase: String? = null
    var img: String?=null
    var link: String?=null
    var amor: Float?=null
    var fav: Int = 0

    constructor(id: Int, nombre: String, nomCie: String, pel: String, clase: String, link: String,
                img: String, amor: Float, fav: Int) : this() {
        this.id = id
        this.nom = nombre
        this.nomCie = nomCie
        this.pel = pel
        this.clase = clase
        this.img = img
        this.link = link
        this.amor = amor
        this.fav = fav
    }

}
