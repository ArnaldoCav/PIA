package com.pia.geoinformatica

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ContentView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Setup()

    }


    private fun Setup() {
        title = "Iniciar Sesion"
        val btn_Iniciar: Button = findViewById(R.id.btn_iniciar)
        val et_email: EditText = findViewById(R.id.et_email)
        val et_password: EditText = findViewById(R.id.et_password)
        btn_Iniciar.setOnClickListener {
            if(et_email.text.isNotEmpty() && et_password.text.isNotEmpty())
            {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(et_email.text.toString(), et_password.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful)
                    {showInicio()}
                    else
                    {showAlert()}
                }
            }
        }

    }

    private fun showAlert() {
        val builder =AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error al iniciar sesion, porfavor verifica que los datos son correctos.")
        builder.setPositiveButton("Aceptar", null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showInicio()
    {
    val inicioIntent = Intent(this,PantallaInicio::class.java)
        startActivity(inicioIntent)
    }
}