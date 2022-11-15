package com.pia.geoinformatica.modelo

import android.location.Address
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pia.geoinformatica.R

class DireccionAdapter(private val direccionList:List<Direccion>, private val onClick:(Direccion) -> Unit):RecyclerView.Adapter<DireccionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DireccionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DireccionViewHolder(layoutInflater.inflate(R.layout.item_direccion, parent, false))
    }

    override fun onBindViewHolder(holder: DireccionViewHolder, position: Int) {
        val item = direccionList[position]
        holder.render(item, onClick)
    }

    override fun getItemCount() = direccionList.size
}