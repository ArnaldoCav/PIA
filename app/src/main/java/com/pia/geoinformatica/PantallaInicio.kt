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
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_pantalla_inicio.*

class PantallaInicio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_inicio)

        val toolbar: Toolbar =findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer=findViewById(R.id.drawer_layout)

        toogle = ActionBarDrawerToggle(this,drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        val navigationView:NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        btnRealizarPedidoMenu.setOnClickListener {
            abrirVentana(Intent(this,BuscarCliente::class.java))
        }

        btnIrVerPedidoMenu.setOnClickListener {
            abrirVentana(Intent(this,VerPedidos::class.java))
        }

        btnAgregarClienteMenu.setOnClickListener {
            abrirVentana(Intent(this,AgregarCliente::class.java))
        }

        btnActualizarClienteMenu.setOnClickListener {
            abrirVentana(Intent(this,ActualizarCliente::class.java))
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_item_cero -> {
                Toast.makeText(this,"Ya estas en esta actividad.",Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        false

    }




}