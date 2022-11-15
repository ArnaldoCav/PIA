package com.pia.geoinformatica.modelo

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pia.geoinformatica.R

class ProductoPedidoViewHolder (view:View):RecyclerView.ViewHolder(view) {

    val nombre = view.findViewById<TextView>(R.id.tvProducto)
    val precio = view.findViewById<TextView>(R.id.tvPrecioProducto)
    val cantidad = view.findViewById<TextView>(R.id.tvCantidad)
    val total = view.findViewById<TextView>(R.id.tvTotalProducto)
    val botonQuitar = view.findViewById<Button>(R.id.btnQuitar)

    fun render(
        productoPedidoModel: ProductoPedido,
        onClickDelete: (Int) -> Unit
    ){
        nombre.text=productoPedidoModel.nombre
        precio.text=productoPedidoModel.precio.toString()
        cantidad.text=productoPedidoModel.cantidad.toString()
        total.text=productoPedidoModel.total.toString()
        botonQuitar.setOnClickListener { onClickDelete(adapterPosition) }

    }

}