package com.turkcell.travelguideapp.view.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.databinding.ActivityMapsBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority
import kotlin.properties.Delegates

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var place: Place

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private var userLocation: LatLng? = null
    private var idFromIntent: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setDefaults()
        initializeEvents()

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

    }

    private fun setDefaults() {
        idFromIntent = intent.getIntExtra("placeId", -1)
    }

    private fun initializeEvents() {
        binding.include.btnBack.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (idFromIntent == -1) {
            binding.btnSaveAndOpen.text = getString(R.string.Save)

            mMap.setOnMapLongClickListener(this)

            locationListener = object : LocationListener {
                override fun onLocationChanged(p0: Location) {

                    userLocation = LatLng(p0.latitude, p0.longitude)
                    mMap.addMarker(MarkerOptions().position(userLocation!!))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation!!, 15f))
                    locationManager!!.removeUpdates(this)
                }

                override fun onProviderDisabled(provider: String) {
                    super.onProviderDisabled(provider)
                }

                override fun onProviderEnabled(provider: String) {
                    super.onProviderEnabled(provider)
                }

            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                konumIstegiBaslat()

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }

            binding.btnSaveAndOpen.setOnClickListener {
                intent.putExtra("fromMapsLocationLatitude", userLocation!!.latitude)
                intent.putExtra("fromMapsLocationLongitude", userLocation!!.longitude)
                setResult(RESULT_OK, intent)
                finish()
            }


        } else {
            //KONUM G??STER BUTONUNA BASINCA ??ALI??ACAK KISIM
            //getPlaceFromId sorunsuz ??al??????rsa burdan sonras?? sorunsuz ??al??????yor.
            place = PlaceLogic.getPlaceFromId(applicationContext, idFromIntent!!)!!
            userLocation = LatLng(place.location.latitude, place.location.longitude)

            binding.btnSaveAndOpen.text = "G??T"
            mMap.addMarker(MarkerOptions().position(userLocation!!).title(place.name))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation!!, 10f))

            binding.btnSaveAndOpen.setOnClickListener {
                // yol tarifi araba ile g??steriliyor. linkin sonu b - >bisiklet, l -> iki tekerlekli, w -> y??r??me
                val gmmIntentUri =
                    Uri.parse("google.navigation:q=${userLocation!!.latitude},${userLocation!!.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                // Hali haz??rda a????lacak bir map uygulamas?? yok ise uygulama crash olur. ??nlemek i??in kullan??yoruz.
                mapIntent.resolveActivity(packageManager)?.let {
                    startActivity(mapIntent)
                }
            }
        }

    }

    override fun onMapLongClick(p0: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(p0))
        userLocation = LatLng(p0.latitude, p0.longitude)
    }

    @SuppressLint("MissingPermission")
    private fun konumIstegiBaslat() {

        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            0.1f,
            locationListener!!
        )

    }

}