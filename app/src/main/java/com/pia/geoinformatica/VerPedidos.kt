package com.pia.geoinformatica

import android.content.Intent
import android.content.res.Configuration
import android.location.Address
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
import com.google.firebase.firestore.Query
import com.pia.geoinformatica.modelo.*
import kotlinx.android.synthetic.main.activity_crear_pedido.*
import kotlinx.android.synthetic.main.activity_ver_pedidos.*
import java.sql.Time
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

class VerPedidos : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer:DrawerLayout
    private lateinit var toogle:ActionBarDrawerToggle

    private val db = FirebaseFirestore.getInstance()
    private var pedidoMutableList: MutableList<Pedido> = PedidoProvider.pedidoList.toMutableList()
    private lateinit var adapter: PedidoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_pedidos)

        val toolbar: Toolbar =findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer=findViewById(R.id.drawer_layout)

        toogle = ActionBarDrawerToggle(this,drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        cargarLista()


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


    private fun cargarLista(){

        db.collection("Ventas").orderBy("fecha", Query.Direction.DESCENDING).get().addOnSuccessListener {
            for(resultado in it)
            {
                val id=resultado.id
                val fecha = resultado.get("fecha").toString()
                val total= resultado.get("total").toString()
                val idCliente = resultado.get("id_cliente").toString()
                var nombre_Cliente= db.collection("Clientes").document(idCliente).get().addOnSuccessListener {
                    val nombreCliente = it.get("nombre").toString() + " " +it.get("apellidos").toString()
                    val calle = it.get("calle").toString()
                    val numero = it.get("numero").toString()
                    val telefonoPedido = it.get("telefono").toString()
                    val idColonia = it.get("id_colonia").toString()
                    val colonia = db.collection("Colonias").document(idColonia).get().addOnSuccessListener {
                        val nombreColonia=it.get("colonia").toString()
                        val cp= it.get("cp").toString()

                        val direccionPedido = calle + " " + numero + " " + nombreColonia + ", cp " + cp + ", Sabinas Hidalgo"

                        val pedido = Pedido(id,fecha, "Cliente: $nombreCliente","Direccion: $direccionPedido","Telefono: $telefonoPedido", "Total: $ $total", idCliente)
                        pedidoMutableList.add(pedido)
                        adapter.notifyItemInserted(pedidoMutableList.size - 1)

                    }



                }

            }

        }
        cargarRecyclerView()
    }

    private fun cargarRecyclerView(){
        adapter = PedidoAdapter(pedidoList = pedidoMutableList, onClick = { onItemSelected(it)})
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, manager.orientation)
        rvPedidos.layoutManager = manager
        rvPedidos.adapter= adapter
        //rvPedidos.addItemDecoration(decoration)
    }

    private fun onItemSelected(pedido:Pedido){
        db.collection("Clientes").document(pedido.idCliente).get().addOnSuccessListener {
            val nombreCliente = it.get("nombre").toString() +" "+ it.get("apellidos")
        val idcolonia = it.get("id_colonia").toString()
            val calle = it.get("calle").toString()
            val numero = it.get("numero").toString()
            val telefono = it.get("telefono").toString()
            val colonia =  db.collection("Colonias").document(idcolonia).get().addOnSuccessListener {
                 val nombreColonia = it.get("colonia").toString()
                val cp = it.get("cp").toString()
                val direccionCliente = calle + " " + numero + " " + nombreColonia + " cp " + cp + " Sabinas Hidalgo"
                showDetalle(direccionCliente.toString(), telefono, pedido.idPedido,nombreCliente )
            }
        }

    }

    private fun showDetalle(direccionCliente:String, telefono:String, idPedido:String, nombreCliente:String)
    {
        val inicioIntent = Intent(this,DetallePedido::class.java)
       inicioIntent.putExtra("direccionCliente", direccionCliente)
        inicioIntent.putExtra("telefono", telefono)
        inicioIntent.putExtra("idPedido", idPedido)
        inicioIntent.putExtra("nombreCliente", nombreCliente)
        startActivity(inicioIntent)

    }

}