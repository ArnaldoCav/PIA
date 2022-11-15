package com.pia.geoinformatica

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.pia.geoinformatica.modelo.*
import kotlinx.android.synthetic.main.activity_detalle_pedido.*
import kotlinx.android.synthetic.main.activity_ver_pedidos.*

class DetallePedido : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle

    private val db = FirebaseFirestore.getInstance()
    private var productoPedidoMutableList:MutableList<ProductoPedido> = ProductoPedidoProvider.ProductoPedidoList.toMutableList()
    private lateinit var adapter:DetallePedidoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pedido)

        val toolbar: Toolbar =findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer=findViewById(R.id.drawer_layout)

        toogle = ActionBarDrawerToggle(this,drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        val navigationView:NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val bundle:Bundle? = intent.extras
        val nombreCliente = bundle?.getString("nombreCliente")?:""
        val direccionCliente = bundle?.getString("direccionCliente")?:""
        val telefono = bundle?.getString("telefono")?:""
        val idPedido = bundle?.getString("idPedido")?:""

        tvClienteDetalle.setText("Cliente: $nombreCliente")
        tvDireccionDetalle.setText("Direccion: $direccionCliente")
        tvTelefonoDetalle.setText("Telefono: $telefono")

        cargarDetalle(idPedido)

        btnMapaDetalle.setOnClickListener { showMap(direccionCliente) }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_item_cero -> {
                abrirVentana(Intent(this,PantallaInicio::class.java))
            }
            R.id.nav_item_one -> {
                abrirVentana(Intent(this,BuscarCliente::class.java))
            }
            R.id.nav_item_two -> {
                abrirVentana(Intent(this,VerPedidos::class.java))
            }
            R.id.nav_item_trhee -> {
                abrirVentana(Intent(this,AgregarCliente::class.java))

            }
            R.id.nav_item_four -> {
                abrirVentana(Intent(this,ActualizarCliente::class.java))

            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)


        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toogle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item))
        {return true}
        return super.onOptionsItemSelected(item)
    }

    private fun abrirVentana(intent: Intent)
    {
        startActivity(intent)
    }


    private fun cargarDetalle (idPedido:String){
        if (idPedido.isNotEmpty())
        {

            cargarLista(idPedido)

            db.collection("Ventas").document(idPedido).get().addOnSuccessListener {
                val subtotal = it.get("subtotal").toString()
                val envio = it.get("envio").toString()
                val total = it.get("total").toString()
                val fecha = it.get("fecha").toString()

                tvSubtotalDetalle.setText("Subtotal: $ $subtotal")
                tvEnvioDetalle.setText("Envio: $ $envio")
                tvTotalDetalle.setText("Total: $ $total")
                tvFechaDetalle.setText(fecha)
            }
        }

    }

    private fun cargarLista (idPedido: String){
        db.collection("Ventas").document(idPedido).collection("detalleVenta").get().addOnSuccessListener {
            for(producto in it)
            {
                val id = producto.id
                val nombre = producto.get("nombre").toString()
                val cantidad = producto.get("cantidad").toString().toInt()
                val total = producto.get("total").toString().toDouble()
                val precio = total.toDouble()/cantidad.toInt()

                productoPedidoMutableList.add(ProductoPedido(id, nombre, precio,cantidad,total))
                //adapter.notifyItemInserted(productoPedidoMutableList.size - 1)
            }
            cargarRecyclerView()
        }

    }


    private fun cargarRecyclerView(){
        adapter = DetallePedidoAdapter(productoPedidoList = productoPedidoMutableList)
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, manager.orientation)
        rvDetalle.layoutManager = manager
        rvDetalle.adapter= adapter
        rvDetalle.addItemDecoration(decoration)
    }

    private fun showMap(direccionCliente:String)
    {
        val inicioIntent = Intent(this,MapsActivity::class.java)
        inicioIntent.putExtra("direccionCliente", direccionCliente)
        startActivity(inicioIntent)

    }



}