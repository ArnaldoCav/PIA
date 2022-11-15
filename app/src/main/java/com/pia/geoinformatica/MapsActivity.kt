package com.pia.geoinformatica

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.pia.geoinformatica.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var lastLocation: Location

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    }

    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var direccionCliente:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle:Bundle? = intent.extras
         direccionCliente = bundle?.getString("direccionCliente")?:""

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap




        map.setOnMarkerClickListener(this)
        map.uiSettings.isZoomControlsEnabled = true

        setUpMap()
    }


    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            Toast.makeText(this, "NO HAY PERMISOS DE LOCALIZACION", Toast.LENGTH_LONG).show()
            return
        } else {
            Toast.makeText(this, "Si hay permiso a la localizacion", Toast.LENGTH_LONG).show()
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location: Location ->

            if (location != null) {
                lastLocation = location
                Toast.makeText(this, "Ubicacion cargada", Toast.LENGTH_LONG).show()
                val currentLatLong = LatLng(location.latitude, location.longitude)
               // map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 13f))
            }

        }

        buscarDireccion()


    }


    private fun buscarDireccion() {
        //val address = "Profr. Miguel Guadiana V. 115 Bellavista, Sabinas Hidalgo CP.65270"
        val address = direccionCliente
        lateinit var userMarkerOptions:MarkerOptions


        if (!TextUtils.isEmpty(address)) {
            val geocoder: Geocoder = Geocoder(this)

            val addressList = geocoder.getFromLocationName(address, 4)

            if (addressList != null) {

                for (address in addressList) {
                    val userAdress: Address = address
                    val latLng: LatLng = LatLng(userAdress.latitude, userAdress.longitude)
                    Toast.makeText(this, latLng.toString(), Toast.LENGTH_LONG).show()

                   //userMarkerOptions.position(latLng)
                   //userMarkerOptions.title("Direccion del cliente")
                   //userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker())
                   //map.addMarker(userMarkerOptions)
                   //map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))


                    map.addMarker(MarkerOptions()
                        .position(latLng)
                        .title("Cliente"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

                }
            }
            else
            {
                Toast.makeText(this, "LISTA VACIA", Toast.LENGTH_LONG).show()
            }




        }

        else
        {
            Toast.makeText(this, "No hay una direccion seleccionada", Toast.LENGTH_LONG).show()
        }


    }
}