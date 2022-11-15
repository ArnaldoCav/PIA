package com.pia.geoinformatica

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.pia.geoinformatica.modelo.Producto
import com.pia.geoinformatica.modelo.ProductoPedido
import com.pia.geoinformatica.modelo.ProductoPedidoAdapter
import com.pia.geoinformatica.modelo.ProductoPedidoProvider
import kotlinx.android.synthetic.main.activity_crear_pedido.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


class CrearPedido : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle

    private val db = FirebaseFirestore.getInstance()
    private var idProducto:String=""

    private var subtotal: Double = 0.0

    private var envio:Double=0.0
    private var totalPedido:Double=0.0

    private var productoPedidoMutableList:MutableList<ProductoPedido> = ProductoPedidoProvider.ProductoPedidoList.toMutableList()
    private lateinit var adapter:ProductoPedidoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_pedido)

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
        val idCliente = bundle?.getString("idCliente")

        btnAgregar.setOnClickListener { agregarProducto()}
        btnSuma.setOnClickListener { sumaCantidad() }
        btnResta.setOnClickListener { restaCantidad() }
        cargarSpinner()
        inirRecyvlerView()
        calcularEnvio(idCliente?:"")

        btnRealizarPedido.setOnClickListener { realizarPedido(idCliente?:"") }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_item_cero -> {
                abrirVentana(Intent(this,PantallaInicio::class.java))
            }
            R.id.nav_item_one -> {
                Toast.makeText(this,"Ya estas en esta actividad.",Toast.LENGTH_SHORT).show()
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


    private fun cargarSpinner(){

        val areas = ArrayList<Producto>()

        db.collection("Productos").get().addOnSuccessListener { resultado->
            for (documento in resultado){
                val producto = documento.get("descripcion").toString()
                val id = documento.id
                val precio = documento.get("precio").toString().toDouble()
                areas.add(Producto(id, producto, precio))
            }

            val adaptador = ArrayAdapter<Producto>(this,android.R.layout.simple_spinner_dropdown_item, areas)
            spProductos.setAdapter(adaptador)


            spProductos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    idProducto=areas.get(p2).id
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }

    }

    private fun inirRecyvlerView(){
        adapter = ProductoPedidoAdapter(productoPedidoList = productoPedidoMutableList, onClickDelete = {position-> onDeletedItem(position)})
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, manager.orientation)
        rv_pedido.layoutManager = manager
        rv_pedido.adapter= adapter
        rv_pedido.addItemDecoration(decoration)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun restaCantidad () {
        var cantidad:Int = etCantidadPedido.text.toString().toInt()
            if(cantidad>1){
                cantidad=cantidad-1
                etCantidadPedido.setText(cantidad.toString())
            }

    }

    private fun sumaCantidad(){
        var cantidad:Int = etCantidadPedido.text.toString().toInt()
        cantidad=cantidad+1
        etCantidadPedido.setText(cantidad.toString())
    }

    private fun agregarProducto() {
        val id = idProducto
        db.collection("Productos").document(id).get().addOnSuccessListener {
            val nombre = it.get("descripcion").toString()
            val precio = it.get("precio").toString().toDouble()
            val cantidad = etCantidadPedido.text.toString().toInt()
            val total = precio * cantidad

            val productoPedido = ProductoPedido(id,nombre,precio,cantidad,total)
            productoPedidoMutableList.add(productoPedido)
            adapter.notifyItemInserted(productoPedidoMutableList.size-1)
            subtotal=subtotal+total
            tvSubtotalPedido.setText("Subtotal: $ $subtotal")
            totalPedido = (subtotal + envio)
            tvTotal.setText("Total: $ $totalPedido")
        }
    }

        private fun onDeletedItem(position: Int) {
            val total:Double = productoPedidoMutableList.get(position).total
            subtotal = subtotal - total
            productoPedidoMutableList.removeAt(position)
            adapter.notifyItemRemoved(position)
            tvSubtotalPedido.setText("Subtotal: $ $subtotal")
            totalPedido = (subtotal + envio)
            tvTotal.setText("Total: $ $totalPedido")
        }

    @SuppressLint("SuspiciousIndentation")
    private fun calcularEnvio(idCliente:String){
        db.collection("Clientes").document(idCliente).get().addOnSuccessListener {
            val idColonia:String = it.get("id_colonia").toString()
            db.collection("Colonias").document(idColonia).get().addOnSuccessListener {
                val zona = it.get("id_zona").toString()
                db.collection("Zonas").document(zona).get().addOnSuccessListener {
                envio = it.get("tarifa").toString().toDouble()
                    tvEnvio.setText("Envio: $ $envio")
                }
            }
        }
    }

    private fun realizarPedido(idCliente:String){
        db.collection("Ventas").add(
            hashMapOf(
                "id_cliente" to idCliente,
                "subtotal" to subtotal,
                "envio" to envio,
                "total" to totalPedido.toString(),
                "fecha" to LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm")),

            )
        ).addOnSuccessListener {
            val idVenta = it.id

            for(productos in productoPedidoMutableList)
            {
                db.collection("Ventas").document(idVenta).collection("detalleVenta").document(productos.idProducto).set(
                    hashMapOf(
                        "nombre" to productos.nombre,
                        "cantidad" to productos.cantidad.toString(),
                        "total" to productos.total.toString()
                    )
                )
            }

            Toast.makeText(this, "Venta guardada con Exito", Toast.LENGTH_SHORT).show()
            showVerPedidos()



        }
    }

    private fun showVerPedidos()
    {
        val inicioIntent = Intent(this,VerPedidos::class.java)

        startActivity(inicioIntent)

    }




    }
