package com.jhr.abdallahsarayrah.findmycar

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        button_save.setOnClickListener {
            val obj = LocationDB(this)
            val db = obj.writableDatabase
            db.execSQL("delete from location")
            db.execSQL("insert into location values(?, ?)", arrayOf(mMap.myLocation.latitude,
                    mMap.myLocation.longitude))

            val location = LatLng(mMap.myLocation.latitude, mMap.myLocation.longitude)
            marker = mMap.addMarker(MarkerOptions().position(location))
        }

        button_get.setOnClickListener {
            val obj = LocationDB(this)
            val db = obj.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM 'location'", arrayOf())
            cursor.moveToFirst()
            marker?.remove()
            marker = mMap.addMarker(MarkerOptions().position(
                    LatLng(cursor.getDouble(cursor.getColumnIndex("lat")),
                            cursor.getDouble(cursor.getColumnIndex("lon")))))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(
                    LatLng(cursor.getDouble(cursor.getColumnIndex("lat")),
                            cursor.getDouble(cursor.getColumnIndex("lon")))))

            Toast.makeText(this, "${cursor.getDouble(0)}, " +
                    "${cursor.getDouble(1)}", Toast.LENGTH_SHORT).show()
        }

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val ll = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                marker?.remove()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }
        }
        lm.requestLocationUpdates("gps", 0, 0f, ll)
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.isMyLocationEnabled = true
    }
}
