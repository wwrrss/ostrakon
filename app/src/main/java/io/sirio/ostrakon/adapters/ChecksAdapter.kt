package io.sirio.ostrakon.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.sirio.ostrakon.R
import io.sirio.ostrakon.models.MiCheck

class ChecksAdapter: RecyclerView.Adapter<ChecksAdapter.ViewHolder>() {

    var list:MutableList<MiCheck> = mutableListOf()

    open fun addData(list: MutableList<MiCheck>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.mi_check_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val miCheck = list.get(position)
        holder.txtNombre.text = miCheck.corrupto.nombre
        holder.txtFecha.text = miCheck.updatedAt.toString()
        Picasso.get()
                .load(miCheck.corrupto.imagen)
                .fit()
                .centerCrop()
                .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtNombre:TextView = view.findViewById(R.id.txtNombre)
        val txtFecha:TextView = view.findViewById(R.id.txtFecha)
        val imageView:ImageView = view.findViewById(R.id.corruptoImageView)
    }
}