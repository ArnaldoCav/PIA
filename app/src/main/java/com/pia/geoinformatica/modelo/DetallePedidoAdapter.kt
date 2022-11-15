package com.pia.geoinformatica.modelo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pia.geoinformatica.R

class DetallePedidoAdapter (private val productoPedidoList:List<ProductoPedido>) :RecyclerView.Adapter<DetallePedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetallePedidoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DetallePedidoViewHolder(layoutInflater.inflate(R.layout.list_item_detalle, parent, false))
    }

    override fun onBindViewHolder(holder: DetallePedidoViewHolder, position: Int) {
        val item = productoPedidoList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = productoPedidoList.size

}