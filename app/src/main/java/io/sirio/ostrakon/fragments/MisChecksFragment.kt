package io.sirio.ostrakon.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.sirio.ostrakon.R
import io.sirio.ostrakon.adapters.ChecksAdapter
import io.sirio.ostrakon.viewmodels.MisChecksViewModel

class MisChecksFragment:Fragment(){

    lateinit var recyclerView: RecyclerView
    lateinit var viewModel:MisChecksViewModel
    var adapter = ChecksAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.mis_checks_fragment,container,false)
        viewModel = ViewModelProviders.of(this).get(MisChecksViewModel::class.java)
        recyclerView = view.findViewById(R.id.listadoRecyclerView)
        recyclerView.setHasFixedSize(true)
        var layoutManager  = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        viewModel.misChecksLiveData.observeForever {
            if(it?.isNotEmpty()!!){
                adapter.addData(it)
            }
        }

        return  view
    }
}