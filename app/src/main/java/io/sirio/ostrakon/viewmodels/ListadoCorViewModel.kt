package io.sirio.ostrakon.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import io.sirio.ostrakon.models.Corrupto
import io.sirio.ostrakon.utils.Constants

class ListadoCorViewModel:ViewModel(){

    var db = FirebaseFirestore.getInstance()

    open var corruptosLiveData:MutableLiveData<List<Corrupto>> = MutableLiveData()

    init {
        getCorruptosList()
    }


    fun getCorruptosList(){
       db.collection(Constants.CORRUPTO_COLLECTION)
               .get()
               .addOnCompleteListener {
                   if(it.isSuccessful){
                       var list:MutableList<Corrupto> = mutableListOf()
                       for(document in it.result){
                           var corrupto = document.toObject(Corrupto::class.java)
                                   .withId<Corrupto>(document.id)
                           list.add(corrupto)
                       }
                       corruptosLiveData.postValue(list)
                   }else{

                   }
               }

    }
    override fun onCleared() {
        super.onCleared()
    }
}