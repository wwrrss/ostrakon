package io.sirio.ostrakon.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import io.sirio.ostrakon.R

class MiPerfilFragment:Fragment(){

    lateinit var txtUserId:TextView

    var auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.mi_perfil_fragment, container,false)
        txtUserId = view.findViewById(R.id.txtUserId)
        if(auth.currentUser==null){
            auth.signInAnonymously().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    setUserId(auth.currentUser?.uid!!)
                }

            }
        }else{
            setUserId(auth.currentUser?.uid!!)
        }

        return view
    }

    private fun setUserId(userId:String){
        txtUserId.text = userId
    }
}