package com.pia.geoinformatica.modelo

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pia.geoinformatica.R

class DetallePedidoViewHolder (view:View):RecyclerView.ViewHolder(view) {

    val nombre = view.findViewById<TextView>(R.id.tvProductoDetalle)
    val precio = view.findViewById<TextView>(R.id.tvPrecioProductoDetalle)
    val cantidad = view.findViewById<TextView>(R.id.tvCantidadDetalle)
    val total = view.findViewById<TextView>(R.id.tvTotalProductoDetalle)


    fun render(
        productoPedidoModel: ProductoPedido
    ){
         nombre.text=productoPedidoModel.nombre
        precio.text=productoPedidoModel.precio.toString()
        cantidad.text=productoPedidoModel.cantidad.toString()
        total.text=productoPedidoModel.total.toString()


    }

}