package com.example.weatherapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CallLog.Locations
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.*

class spalsh : AppCompatActivity() {
    lateinit var mfusedlocation: FusedLocationProviderClient //it is a libraryThe main entry point for interacting with the fused location provider.
    private var myRequestCode=1010 //its help to  identify  which permisssion request being responded to

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()   // it will tell our last location  it is a non-blocking method which means it runs  background and does not block the main thread.
//        Handler(Looper.getMainLooper()).postDelayed({
//            var intent=Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        },2000)
//
////        val im=findViewById<ImageView>(R.id.img_id)
//        im.alpha=1f
//        im.animate().setDuration(2000).alpha(0f).withEndAction(){
//            startActivity(Intent(this,MainActivity::class.java))
//            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)


//        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(checkpermisson()){
            if(LocationEnable()){
                mfusedlocation.lastLocation.addOnCompleteListener{
                        task ->
                    var location:Location?=task.result
                    if(location==null)
                    {
                        newLocation()
                    }else{
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent=Intent(this,MainActivity::class.java)
                            intent.putExtra("lat",location.latitude.toString())
                            intent.putExtra("long",location.longitude.toString())
                            startActivity(intent)
                            finish()
                        },2000)

                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on your location",Toast.LENGTH_LONG).show()
            }

        }else{
            requestGpsPermission()
        }
    }
   @SuppressLint("MissingPermission")
   private fun newLocation(){
           var locationRequest=LocationRequest()//  //such as navigation or location-based services. may consume more battery power.
       locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
           locationRequest.interval=0
           locationRequest.fastestInterval=0
           locationRequest.numUpdates=1
           mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
           mfusedlocation.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper()!!)
//       PRIORITY_HIGH_ACCURACY is more likely to use GPS, and PRIORITY_BALANCED_POWER_ACCURACY is more likely to use WIFI & Cell tower positioning,

   }private val locationCallback=object: LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation:Location=p0.lastLocation
//            onLocationResult() method. This method is called when the device's location has changed, and it is passed a LocationResult object that
//            contains information about the location, such as the last known location. The developer can then use this information to update their app's
//            functionality or UI
        }
    }
    private fun LocationEnable(): Boolean{
        //checked you have allow permission maded
        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager //as keyword is used to cast the result of getSystemService()
        // method to LocationManager object.
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
   //layoutmanager:-This object can be used to determine the device's current location, as well as to listen for changes in location.
        //getsystemservice:- its is a method and  call the operating system
    }
    private fun requestGpsPermission(){ //asking to user displaying the particular window
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,//Requests permissions to be granted to this application
            android.Manifest.permission.ACCESS_FINE_LOCATION),myRequestCode)
    }
    private fun checkpermisson(): Boolean{
        if( //allow permission check
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
        }

        override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)//Callback for the result from requesting permissions.
            // This method is invoked for every call on requestPermissions(Activity, String[], int).
//            Note: It is possible that the permissions request interaction with the user is interrupted. In this case you will receive empty permissions
//            and results arrays which should be treated as a cancellation.
        if(requestCode==myRequestCode)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation()
            }
        }
    }
}