package com.pia.geoinformatica

import android.content.ClipData.Item
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.pia.geoinformatica.modelo.Colonia
import kotlinx.android.synthetic.main.activity_agregar_cliente.*

class AgregarCliente : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle

    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cliente)

        val toolbar: Toolbar =findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer=findViewById(R.id.drawer_layout)

        toogle = ActionBarDrawerToggle(this,drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        val navigationView:NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        Cargar_Spinner()
        Guardar_Cliente()
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
                Toast.makeText(this,"Ya estas en esta actividad.",Toast.LENGTH_SHORT).show()

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

    private fun Cargar_Spinner(){

            val areas = ArrayList<Colonia>()


            db.collection("Colonias").get().addOnSuccessListener { resultado->
                for (documento in resultado){
                    val colonia = documento.get("colonia").toString()
                    val id = documento.id
                    areas.add(Colonia(id, colonia))

                }

                val adaptador = ArrayAdapter<Colonia>(this,android.R.layout.simple_spinner_dropdown_item, areas)
                
                spinner.setAdapter(adaptador)

                  spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            tvId.setText(areas.get(p2).id)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }
            }

        }

    private fun Cargar_Cliente(){
        db.collection("Clientes").document("JUq2wbsldfgypmI7byrw").get().addOnSuccessListener {


            db.collection("Colonias").document(it.get("id_colonia") as String).get().addOnSuccessListener {
                tv.setText(it.get("colonia") as String?)
            }
        }

    }

    private fun Guardar_Cliente() {

        btn_Guardar.setOnClickListener {

            if (et_telefono.text.toString().isNotEmpty() && et_nombre.text.toString()
                    .isNotEmpty() && et_calle.text.toString()
                    .isNotEmpty() && et_numero.text.toString().isNotEmpty()
            ) {
                db.collection(("Clientes")).document().set(
                    hashMapOf(
                        "telefono" to et_telefono.text.toString(),
                        "nombre" to et_nombre.text.toString(),
                        "apellidos" to et_apellidos.text.toString(),
                        "calle" to et_calle.text.toString(),
                        "numero" to et_numero.text.toString(),
                        "id_colonia" to tvId.text.toString(),
                        "entreCalles" to et_entreCalles.text.toString(),
                        "referencias" to et_referencias.text.toString()
                    )
                ).addOnSuccessListener {
                    Toast.makeText(this, "Usuario Guardado", Toast.LENGTH_LONG).show()
                    et_telefono.text.clear()
                    et_nombre.text.clear()
                    et_apellidos.text.clear()
                    et_calle.text.clear()
                    et_numero.text.clear()
                    et_entreCalles.text.clear()
                    et_referencias.text.clear()
                }


            }

            else{
                Toast.makeText(this, "ERROR - PORFAVOR REVISA LOS DATOS FALTANTES", Toast.LENGTH_LONG).show()
            }

        }

    }

    }




