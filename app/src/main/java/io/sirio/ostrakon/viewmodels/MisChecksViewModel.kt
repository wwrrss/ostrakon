package io.sirio.ostrakon.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.sirio.ostrakon.models.Corrupto
import io.sirio.ostrakon.models.MiCheck
import io.sirio.ostrakon.utils.Constants

class MisChecksViewModel:ViewModel(){

    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    open var misChecksLiveData: MutableLiveData<MutableList<MiCheck>> = MutableLiveData()

    init {
        if(auth.currentUser==null){
            auth.signInAnonymously().addOnCompleteListener { task ->
                getData()
            }
        }else{
            getData()
        }

    }

    fun getData(){
        if(auth.currentUser == null){return}
        db.collection(Constants.CHECKS_COLLECTION)
                .whereEqualTo("userId",auth.currentUser?.uid)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        var list:MutableList<MiCheck> = it.getResult()?.toObjects(MiCheck::class.java)!!
                        misChecksLiveData.postValue(list)
                    }
                }
    }
}