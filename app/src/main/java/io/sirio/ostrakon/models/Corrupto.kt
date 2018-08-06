package io.sirio.ostrakon.models

import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

class Corrupto:Model(),Serializable{
    var nombre = ""
    var imagen = ""

    @Transient
    var punto:GeoPoint? = null

    var descripcion = ""
}