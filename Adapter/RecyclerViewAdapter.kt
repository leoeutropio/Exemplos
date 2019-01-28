package com.str0nd4.ceep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.str0nd4.ceep.R
import com.str0nd4.ceep.model.Notas
import kotlinx.android.synthetic.main.layout_itemnota.view.*

class RecyclerViewAdapter(private val notas : ArrayList<Notas>,
                          private val context : Context) : Adapter<RecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_itemnota, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nota = notas[position]
        holder.titulo.text = nota.titulo
        holder.descricao.text = nota.descricao
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val titulo  = itemView.itemnota_titulo!!
        val descricao = itemView.itemnota_descricao!!
    }

}