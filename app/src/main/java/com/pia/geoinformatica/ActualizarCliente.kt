package com.pia.geoinformatica

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
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.pia.geoinformatica.modelo.Colonia
import kotlinx.android.synthetic.main.activity_actualizar_cliente.*
import kotlinx.android.synthetic.main.activity_agregar_cliente.*
import kotlinx.android.synthetic.main.activity_buscar_cliente.*

class ActualizarCliente : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle

    private val db = FirebaseFirestore.getInstance()
    private var idClienteActualizar:String=""
    private var idColoniaActual=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_cliente)

        val toolbar: Toolbar =findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer=findViewById(R.id.drawer_layout)

        toogle = ActionBarDrawerToggle(this,drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

buscar()
        Actualizar_Cliente()
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
                Toast.makeText(this,"Ya estas en esta actividad.",Toast.LENGTH_SHORT).show()

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

    private fun buscar(){

        btn_buscarClienteActualizar.setOnClickListener {
            db.collection("Clientes").whereEqualTo("telefono",  et_buscarClienteActualizar.text.toString()).get().addOnSuccessListener {
                for(documento in it)
                {
                    idClienteActualizar = documento.id
                    idColoniaActual=documento.get("id_colonia").toString()
                    tv_idClienteActualizar.setText(idClienteActualizar)
                    et_TelefonoActualizar.setText(documento.get("telefono").toString())
                    et_nombreCActializar.setText(documento.get("nombre").toString())
                    et_apellidosActualizar.setText(documento.get("apellidos").toString())
                    et_CalleActualizar.setText(documento.get("calle").toString())
                    et_numeroActualizar.setText(documento.get("numero").toString())
                    et_EntreCallesActualizar.setText(documento.get("entreCalles").toString())
                    et_ReferenciasActualizar.setText(documento.get("referencias").toString())
                    db.collection("Colonias").document(documento.get("id_colonia").toString()).get().addOnSuccessListener {
                        tv_ColoniaActualizar.setText(it.get("colonia").toString())
                    }

                    cb_CambiaColonia.setOnClickListener {
                        if(cb_CambiaColonia.isChecked) {
                            Cargar_Spinner()
                        }
                    }

                }
            }
        }

    }


    private fun Cargar_Spinner(){

        val areas = ArrayList<Colonia>()


        db.collection("Colonias").get().addOnSuccessListener { resultado->
            for (documento in resultado){
                val colonia = documento.get("colonia").toString()
                val id = documento.id
                areas.add(Colonia(id, colonia))
            }

            val adaptador = ArrayAdapter<Colonia>(this,android.R.layout.simple_spinner_dropdown_item, areas)
            spinner2Actualizar.setAdapter(adaptador)


            spinner2Actualizar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    tvID_Actualizar.setText(areas.get(p2).id)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }

    }

    private fun Actualizar_Cliente() {

        btn_ActualizarCliente.setOnClickListener {

            if (et_TelefonoActualizar.text.toString().isNotEmpty() && et_nombreCActializar.text.toString()
                    .isNotEmpty() && et_CalleActualizar.text.toString()
                    .isNotEmpty() && et_numeroActualizar.text.toString().isNotEmpty()
            ) {

                if (cb_CambiaColonia.isChecked)
                {
                    db.collection("Clientes").document(idClienteActualizar).set(
                        hashMapOf("telefono" to et_TelefonoActualizar.text.toString(),
                            "nombre" to et_nombreCActializar.text.toString(),
                            "apellidos" to et_apellidosActualizar.text.toString(),
                            "calle" to et_CalleActualizar.text.toString(),
                            "numero" to et_numeroActualizar.text.toString(),
                            "entreCalles" to et_EntreCallesActualizar.text.toString(),
                            "referencias" to et_ReferenciasActualizar.text.toString(),
                            "id_colonia" to tvID_Actualizar.text.toString())
                    ).addOnSuccessListener { Toast.makeText(this, "Cliente Actualizado", Toast.LENGTH_LONG).show() }
                }
                else
                {
                    db.collection("Clientes").document(idClienteActualizar).set(
                        hashMapOf("telefono" to et_TelefonoActualizar.text.toString(),
                            "nombre" to et_nombreCActializar.text.toString(),
                            "apellidos" to et_apellidosActualizar.text.toString(),
                            "calle" to et_CalleActualizar.text.toString(),
                            "numero" to et_numeroActualizar.text.toString(),
                            "entreCalles" to et_EntreCallesActualizar.text.toString(),
                            "referencias" to et_ReferenciasActualizar.text.toString(),
                            "id_colonia" to idColoniaActual)).addOnSuccessListener { Toast.makeText(this, "Cliente Actualizado", Toast.LENGTH_LONG).show() }
                }

            }

            else{
                Toast.makeText(this, "ERROR - PORFAVOR REVISA LOS DATOS FALTANTES", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun abrirVentana(intent: Intent)
    {
        startActivity(intent)
    }


}