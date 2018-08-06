package io.sirio.ostrakon.fragments

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.sirio.ostrakon.R
import io.sirio.ostrakon.models.Corrupto
import android.widget.Button
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import io.sirio.ostrakon.models.CheckIn
import io.sirio.ostrakon.viewmodels.CorruptoDetailViewModel


class CorruptoDetailFragment:Fragment(), OnMapReadyCallback, LocationListener{

    lateinit var fragment: SupportMapFragment

    lateinit var txtDescripcion:TextView

    lateinit var buttonCheckIn:Button

    lateinit var locationManager: LocationManager

    lateinit var viewModel:CorruptoDetailViewModel

    var corrupto:Corrupto? = null

    var map:GoogleMap? = null

    val REQUEST_PERMISSION_MY_LOCATION_GMAPS = 12

    val REQUEST_PERMISSION_CURRENT_LOCATION = 13

    //SI DISTANCIA MENOR A ESTE VALOR PODEMOS ENVIAR COMO UN CHECK-IN
    //ESTA DISTANCIA ES TOTALMENTE ARBITRARIA
    val DISTANCE_THRESHOLD = 300f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.corrupto_detail_fragment,container,false)
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        viewModel = ViewModelProviders.of(this).get(CorruptoDetailViewModel::class.java)
        fragment =  childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragment.getMapAsync(this)
        corrupto =  arguments?.getSerializable("c") as? Corrupto
        txtDescripcion = view.findViewById(R.id.txtDescripcion)
        txtDescripcion.text = corrupto?.descripcion
        buttonCheckIn = view.findViewById(R.id.buttonCheckIn)
        buttonCheckIn.setOnClickListener{onClickOnChekIn(view)}
        return view
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        if(corrupto!=null&&corrupto?.punto!=null){
            val sid = LatLng(corrupto?.punto?.latitude!!, corrupto?.punto?.longitude!!)
            map?.addMarker(MarkerOptions().position(sid))
                    ?.title = corrupto?.nombre

            var mapUi = map?.uiSettings
            mapUi?.isZoomControlsEnabled = true
            mapUi?.isCompassEnabled = true
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(sid,15f))
            enableMyLocation()
        }

    }

    open fun onClickOnChekIn(view:View){
        startListeningCurrentLocation()
    }
    private fun enableMyLocation(){
        if(ContextCompat.checkSelfPermission(context!!,android.Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED){
            val permissions:Array<String> = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissions,REQUEST_PERMISSION_MY_LOCATION_GMAPS)
        }else{
            map?.isMyLocationEnabled = true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_PERMISSION_MY_LOCATION_GMAPS ){
            if(!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                enableMyLocation()
            }
        }
    if(requestCode == REQUEST_PERMISSION_CURRENT_LOCATION ){
            if(!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startListeningCurrentLocation()
            }
        }
    }

    private fun startListeningCurrentLocation(){
        if(!isGpsEnabled()){return}
        if(ContextCompat.checkSelfPermission(context!!,android.Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED){
            val permissions:Array<String> = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissions,REQUEST_PERMISSION_CURRENT_LOCATION)
        }else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0f,this)

           var location =  locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if(location!=null){
                calculateDistance(location)
            }

        }

    }

    private fun stopListeningCurrentLocation(){
        locationManager.removeUpdates(this)
    }

    private fun isGpsEnabled():Boolean{

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showDialog("Por favor activa el GPS para poder realizar el CHECK-IN")
            return false
        }

        return true

    }

    private fun showDialog(message:String){
        var dialog = AlertDialog.Builder(context)
        dialog.setMessage(message)
        dialog.setPositiveButton("Ok" ){ dialogInterface, i ->
            val intent =  Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent)
        }
        dialog.setNegativeButton("No!",null)
        dialog.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopListeningCurrentLocation()
    }

    private fun calculateDistance(location: Location?){
        Log.d("d",location?.latitude.toString())
        Log.d("d",location?.longitude.toString())
        Log.d("d",location?.accuracy.toString())
        var corruptoLocation = Location("GPS")
        if(corrupto?.punto==null){
            return
        }
        corruptoLocation.latitude = corrupto?.punto?.latitude!!
        corruptoLocation.longitude = corrupto?.punto?.longitude!!

        var distance =  location?.distanceTo(corruptoLocation)
        Log.d("distance es",distance.toString())

        if(distance!!>DISTANCE_THRESHOLD){
            Toast.makeText(context!!,"Estas a $distance metros del local!!! ",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context!!,"SUPER!!!! CHECK-IN ! ",Toast.LENGTH_LONG).show()
            var checkIn = CheckIn()
            checkIn.corruptoId = corrupto?.id!!
            checkIn.punto = GeoPoint(location?.latitude!!,location?.longitude!!)
            checkIn.corrupto = corrupto!!
            viewModel.checkIn(checkIn)
        }


        stopListeningCurrentLocation()
    }

    override fun onLocationChanged(location: Location?) {
        calculateDistance(location)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }
}