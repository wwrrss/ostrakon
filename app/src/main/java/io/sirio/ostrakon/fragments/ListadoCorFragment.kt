package io.sirio.ostrakon.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import io.sirio.ostrakon.R
import io.sirio.ostrakon.adapters.ListadoCorruptosAdapter

import io.sirio.ostrakon.viewmodels.ListadoCorViewModel

class ListadoCorFragment:Fragment(){

    lateinit var viewModel: ListadoCorViewModel

    lateinit var recyclerView: RecyclerView

    lateinit var adapter:ListadoCorruptosAdapter

    lateinit var imageViewHeader:ImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(ListadoCorViewModel::class.java)
        val view = inflater.inflate(R.layout.listado_fragment,container,false)
        imageViewHeader = view.findViewById(R.id.imageHeader)
        Picasso.get()
                .load("http://www.lgpn.ox.ac.uk/image_archive/other/figures/O.4%20ARISTEIDES.jpg")
                .resize(300,300)
                .centerInside()
                .into(imageViewHeader)

        adapter = ListadoCorruptosAdapter(arrayListOf(),context!!)
        recyclerView = view.findViewById(R.id.listadoRecyclerView)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter


        viewModel.corruptosLiveData.observeForever { l ->
            adapter.addData(l!!)
        }

        return view
    }
}

