package io.sirio.ostrakon.models

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

class MiCheck:Serializable{

    @ServerTimestamp
    var updatedAt:Date = Date()

    var punto:GeoPoint = GeoPoint(0.0,0.0)

    var corrupto:Corrupto = Corrupto()

    var userId = ""
}