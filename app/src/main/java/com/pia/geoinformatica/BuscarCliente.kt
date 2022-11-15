package com.pia.geoinformatica

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_buscar_cliente.*

class BuscarCliente : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer:DrawerLayout
    private lateinit var toogle:ActionBarDrawerToggle

    private val db = FirebaseFirestore.getInstance()
    private var idCliente:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_cliente)

        val toolbar:Toolbar=findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer=findViewById(R.id.drawer_layout)

        toogle = ActionBarDrawerToggle(this,drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        val navigationView:NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        buscar()
        btn_Editar.setOnClickListener { showActuaLizar() }
        btn_siguiente.setOnClickListener{showPedido(idCliente)}
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




    private fun buscar(){

        btn_buscarCliente.setOnClickListener {
            db.collection("Clientes").whereEqualTo("telefono",  et_buscarCliente.text.toString()).get().addOnSuccessListener {
                for(documento in it)
                {
                    idCliente=documento.id
                    tv_idCliente.setText("Id: " + documento.id)
                    et_TelefonoA.setText(documento.get("telefono").toString())
                    et_nombreC.setText(documento.get("nombre").toString())
                    et_apellidosA.setText(documento.get("apellidos").toString())
                    et_CalleA.setText(documento.get("calle").toString())
                    et_numeroA.setText(documento.get("numero").toString())
                    et_EntreCallesA.setText(documento.get("entreCalles").toString())
                    et_ReferenciasA.setText(documento.get("referencias").toString())

                    db.collection("Colonias").document(documento.get("id_colonia").toString()).get().addOnSuccessListener {
                        et_Colonia.setText(it.get("colonia").toString())
                    }

                }
            }
        }

    }

    private fun showActuaLizar()
    {
            val inicioIntent = Intent(this,ActualizarCliente::class.java)
            startActivity(inicioIntent)


    }

    private fun showPedido(id:String)
    {
        if (id.isNotEmpty())
        {
            val inicioIntent = Intent(this,CrearPedido::class.java).apply {
                putExtra("idCliente", id)
            }
            startActivity(inicioIntent)
        }
        else
        {
            showAlert()
        }



    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error al enviar el identificador del cliente, busca un cliente e intentalo de nuevo.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }



}


