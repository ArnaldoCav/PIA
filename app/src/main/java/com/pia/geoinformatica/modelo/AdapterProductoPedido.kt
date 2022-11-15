package com.pia.geoinformatica.modelo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.api.Context
import com.pia.geoinformatica.R
import kotlinx.android.synthetic.main.list_item.view.*

class AdapterProductoPedido (private val mContext: android.content.Context, private val listaProductos:List<ProductoPedido>) :ArrayAdapter<ProductoPedido>(mContext,0,listaProductos){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val layout = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false)

        val producto = listaProductos[position]
        layout.tvProducto.text=producto.nombre
        layout.tvPrecioProducto.text=producto.precio.toString()
        layout.tvCantidad.text=producto.cantidad.toString()
        layout.tvTotalProducto.text=producto.total.toString()

        return layout
    }
}