package io.sirio.ostrakon.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import android.support.annotation.NonNull



@IgnoreExtraProperties
open class Model {

    @Exclude
    var id = ""

    fun <T : Model> withId(id: String): T {
        this.id = id
        return this as T
    }
}