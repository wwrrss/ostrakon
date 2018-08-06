package io.sirio.ostrakon.viewmodels

import android.arch.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.sirio.ostrakon.models.CheckIn
import io.sirio.ostrakon.utils.Constants

class CorruptoDetailViewModel:ViewModel(){
    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    var user:FirebaseUser? = null

    init {
        if(auth.currentUser==null){
            auth.signInAnonymously().addOnCompleteListener { task ->
                if(task.result.user!=null){
                    user = task.result.user
                }
            }
        }else{
            user = auth.currentUser
        }
    }

    fun checkIn(data:CheckIn){
        data.userId = user?.uid!!

        db.collection(Constants.CHECKS_COLLECTION)
                .add(data)
                .addOnCompleteListener  {

                }
                .addOnFailureListener {

                }

    }

}