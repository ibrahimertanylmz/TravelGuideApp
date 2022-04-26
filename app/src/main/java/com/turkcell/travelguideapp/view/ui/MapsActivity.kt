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
import androidx.activity.result.contract.ActivityResultContracts
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

    private var dbLatitude by Delegates.notNull<Double>()
    private var dbLongitude by Delegates.notNull<Double>()
    private lateinit var place: Place

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private var selectedLatitude: Double = 0.0
    private var selectedLongitude: Double = 0.0
    private var userLocation:LatLng?=null

    private var getId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initializeEvents()

        getId=intent.getIntExtra("placeId",-1)

        // DENEMEK İÇİN YAPILDI SİLİNECEK
        var latLng = LatLng(122.3, 123.3)
        place = Place("isim", latLng, "tanım kısa olanından", "açıklama", Priority.ONE)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

    }

    private fun initializeEvents() {
        binding.include.btnBack.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // ÇALIŞIYOR SIKINTI YOK
        if (getId==-1) {
            binding.btnSaveAndOpen.text = getString(R.string.Save)

            mMap.setOnMapLongClickListener(this)


            locationListener = object : LocationListener {
                override fun onLocationChanged(p0: Location) {

                    userLocation = LatLng(p0.latitude, p0.longitude)
                    mMap.addMarker(MarkerOptions().position(userLocation!!))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation!!, 15f))
                    locationManager!!.removeUpdates(this) // bir kere konumu aldıktan sonra konum izlemeyi bırakır.
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

                var intent=Intent(this,MainActivity::class.java)
                //düznelenecek
                intent.putExtra("fromMapsLocationLatitude",userLocation!!.latitude)
                intent.putExtra("fromMapsLocationLongitude",userLocation!!.longitude)
                setResult(RESULT_OK,intent)

                finish()
            }


        } else { //KONUM GÖSTER BUTONUNA BASINCA ÇALIŞACAK KISIM
            //getPlaceFromId sorunsuz çalışırsa burdan sonrası sorunsuz çalışıyor.
            place=PlaceLogic.getPlaceFromId(applicationContext,getId!!)!!

            dbLatitude = place.location.latitude
            dbLongitude = place.location.longitude

            binding.btnSaveAndOpen.setText("GİT")
            val yourPlace = LatLng(dbLatitude, dbLongitude)
            mMap.addMarker(MarkerOptions().position(yourPlace).title(place.name))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourPlace, 10f))

            binding.btnSaveAndOpen.setOnClickListener {
                // yol tarifi araba ile gösteriliyor. linkin sonu b - >bisiklet, l -> iki tekerlekli, w -> yürüme
                val gmmIntentUri = Uri.parse("google.navigation:q=${dbLatitude},${dbLongitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                // Hali hazırda açılacak bir map uygulaması yok ise uygulama crash olur. Önlemek için kullanıyoruz.
                mapIntent.resolveActivity(packageManager)?.let {
                    startActivity(mapIntent)
                }
            }
        }

    }

    override fun onMapLongClick(p0: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(p0))

        selectedLatitude = p0.latitude
        selectedLongitude = p0.longitude

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