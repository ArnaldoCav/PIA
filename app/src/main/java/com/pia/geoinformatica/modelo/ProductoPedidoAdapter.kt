package com.pia.geoinformatica.modelo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pia.geoinformatica.R

class ProductoPedidoAdapter (private val productoPedidoList:List<ProductoPedido>, private val onClickDelete:(Int)-> Unit) :RecyclerView.Adapter<ProductoPedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoPedidoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductoPedidoViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProductoPedidoViewHolder, position: Int) {
        val item = productoPedidoList[position]
        holder.render(item, onClickDelete)
    }

    override fun getItemCount(): Int = productoPedidoList.size

}