package io.sirio.ostrakon.models

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

class CheckIn:Serializable{
    var userId = ""
    var corruptoId = ""
    @ServerTimestamp
    var updatedAt = Date()
    var punto = GeoPoint(0.0,0.0)
    var corrupto =Corrupto()
}