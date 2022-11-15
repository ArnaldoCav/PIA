package com.pia.geoinformatica.modelo

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pia.geoinformatica.R

class PedidoViewHolder (view:View):RecyclerView.ViewHolder(view){
    val fecha = view.findViewById<TextView>(R.id.tvFecha)
    val nombrePedido = view.findViewById<TextView>(R.id.tvNombrePedido)
    val direccionPedido = view.findViewById<TextView>(R.id.tvDireccionPedido)
    val telefonoPedido = view.findViewById<TextView>(R.id.tvTelefonoPedido)
    val totalPedido = view.findViewById<TextView>(R.id.tvTotalPedido)

    fun render(
        pedidoModel: Pedido,
        onClick: (Pedido) -> Unit
    ){
        fecha.text=pedidoModel.fecha.toString()
        nombrePedido.text=pedidoModel.nombre.toString()
        direccionPedido.text=pedidoModel.direccion
        telefonoPedido.text=pedidoModel.telefono
        totalPedido.text=pedidoModel.total.toString()

        itemView.setOnClickListener { onClick(pedidoModel) }

    }
}