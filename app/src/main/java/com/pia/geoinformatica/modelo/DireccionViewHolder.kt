package com.pia.geoinformatica.modelo

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pia.geoinformatica.R

class DireccionViewHolder  (view: View): RecyclerView.ViewHolder(view){
    val direccion = view.findViewById<TextView>(R.id.tvDireccion)

    fun render(direccionModel:Direccion, onClick: (Direccion) -> Unit){
        direccion.text=direccionModel.direccion.toString()
        itemView.setOnClickListener{onClick(direccionModel)}
    }
}