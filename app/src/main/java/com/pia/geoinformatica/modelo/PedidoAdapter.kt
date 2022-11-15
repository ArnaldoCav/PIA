package com.pia.geoinformatica.modelo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pia.geoinformatica.R

class PedidoAdapter(private val pedidoList: List<Pedido>, private val onClick:(Pedido)->Unit):RecyclerView.Adapter<PedidoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PedidoViewHolder(layoutInflater.inflate(R.layout.list_pedido_item, parent, false))
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val item = pedidoList[position]
        holder.render(item, onClick)
    }

    override fun getItemCount(): Int = pedidoList.size
}