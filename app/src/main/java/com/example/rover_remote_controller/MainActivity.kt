package com.example.rover_remote_controller

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var telemetryText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        telemetryText = findViewById(R.id.telemetryText)

        // Initialize Joystick
        val joystick = findViewById<JoystickView>(R.id.joystickView)
        joystick.onJoystickMoveListener = { x, y ->
            telemetryText.text = String.format("X: %.2f, Y: %.2f", x, y)
            // TODO: Send movement commands to rover
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Default position (e.g., Silicon Valley or your rover's start point)
        val roverPos = LatLng(37.422, -122.084)
        mMap.addMarker(MarkerOptions().position(roverPos).title("Rover Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(roverPos, 15f))
        mMap.uiSettings.isZoomControlsEnabled = false
    }
}
