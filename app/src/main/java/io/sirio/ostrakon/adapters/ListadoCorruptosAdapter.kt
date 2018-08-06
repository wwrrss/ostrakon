package io.sirio.ostrakon.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.sirio.ostrakon.R
import io.sirio.ostrakon.models.Corrupto
import io.sirio.ostrakon.utils.ListadoClickMessage
import org.greenrobot.eventbus.EventBus

class ListadoCorruptosAdapter(private var list:MutableList<Corrupto>, var context: Context):RecyclerView.Adapter<ListadoCorruptosAdapter.ViewHolder>(){


    open fun addData(listLocal: List<Corrupto>){
        list.clear()
        list.addAll(listLocal)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listado_corruptos_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val corrupto = list.get(position)
        holder.txtNombre.text = corrupto.nombre
        holder.itemView.tag = corrupto
        Picasso.get()
                .load(corrupto.imagen)

                .fit()
                .centerCrop()
                .into(holder.imageViewBg)
    }

    override fun getItemCount(): Int {
        return list.count()
    }



    class ViewHolder(val view:View):RecyclerView.ViewHolder(view){
        val txtNombre:TextView = view.findViewById(R.id.txtNombre)
        val imageViewBg:ImageView = view.findViewById(R.id.imgBackground)
        init {
            view.setOnClickListener { v->
                EventBus.getDefault().post(ListadoClickMessage(v.tag as Corrupto))
            }
        }
    }
}