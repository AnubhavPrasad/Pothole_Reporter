package com.example.potholereporter
import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.potholereporter.databinding.FragmentTitleBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

private val REQUEST_PERMISSSION=10
class TitleFragment : Fragment() {
    lateinit var latitude:String    //Current Latitude
    lateinit var longitude:String   //Current Longitude
    lateinit var LocationInput: String   // Location input from user stored
    lateinit var Desc: String        //Description stored
    lateinit var imagebit: Bitmap   //Captured Image stored as Bitmap
    private var REQUEST_IMAGE_CAPTURE = 1
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var binding: FragmentTitleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)
        Log.i("MainActivity","INFLATED")
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context?.let { checkpermission(permissions, it) }!!) {
                enableview()
            } else {
                requestPermissions(permissions, REQUEST_PERMISSSION)
                enableview()
            }
        } else {
            enableview()
        }
        binding.clickButton.setOnClickListener {
            clickimage()        //Call for the capture of image
        }
        binding.submtButton.setOnClickListener {
            SubmitAction()
        }
        return binding.root
    }

    private fun enableview() {
        Log.i("MainActivity","ENABLEVIEW")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        binding.currentButton.setOnClickListener {
            getlocation()
        }

    }

    private fun getlocation() {
        Log.i("MainActivity","GET LOCATION CALLED")
        fusedLocationClient.lastLocation.addOnSuccessListener { location->
            if(location!=null){
                Log.i("MainActivity","INSIDE")
                latitude=location.latitude.toString()
                longitude=location.longitude.toString()
                Log.i("MainActivity",latitude)
                Log.i("MainActivity",longitude)
            }
        }
    }

    private fun checkpermission(permissionArray: Array<String>,context: Context): Boolean {
        var allsuccess=true
        for(i in permissionArray.indices){
            if(checkCallingOrSelfPermission(context,permissionArray[i]) == PermissionChecker.PERMISSION_DENIED)
                allsuccess = false
        }
        return allsuccess

    }

    private fun SubmitAction() {
        LocationInput = binding.locationId.text.toString() //Input of location from user
        Desc = binding.shortId.text.toString() //Input of description from user
        Log.i("MainActivity", LocationInput)
        Log.i("MainActivity", Desc)
        }

    private fun clickimage() {
        var imageIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(imageIntent, REQUEST_IMAGE_CAPTURE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var extras = data?.extras
        imagebit = extras?.get("data") as Bitmap
        binding.showImage.setImageBitmap(imagebit)
    }
}
