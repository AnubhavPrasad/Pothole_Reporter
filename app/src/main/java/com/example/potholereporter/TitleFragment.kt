package com.example.potholereporter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.potholereporter.databinding.FragmentTitleBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

private val REQUEST_PERMISSSION = 10

@Suppress("UNREACHABLE_CODE")
class TitleFragment : Fragment() {
    lateinit var databaseReference: FirebaseDatabase
    lateinit var storageReference: FirebaseStorage
    private var backpressedtime = 0L
    lateinit var toast: Toast
    private var latitude = ""   //Current Latitude
    private var longitude = ""   //Current Longitude
    lateinit var LocationInput: String   // Location input from user stored
    lateinit var Desc: String        //Description stored
    lateinit var imagebit: Bitmap   //Captured Image stored as Bitmap
    lateinit var refdata: DatabaseReference
    lateinit var id: String

    private var REQUEST_IMAGE_CAPTURE = 1
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var binding: FragmentTitleBinding
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)
        Log.i("MainActivity", "INFLATED")
        databaseReference = FirebaseDatabase.getInstance()
        storageReference = FirebaseStorage.getInstance()

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
        setHasOptionsMenu(true)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (backpressedtime + 2000 > System.currentTimeMillis()) {
                toast.cancel()
                requireActivity().finish()

            } else {
                toast = Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT)
                toast.show()
            }
            backpressedtime = System.currentTimeMillis()

        }
        callback.isEnabled
        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    private fun enableview() {
        Log.i("MainActivity", "ENABLE VIEW")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        binding.currentButton.setOnClickListener {
            getlocation()
        }
    }

    private fun getlocation() {
        Log.i("MainActivity", "GET LOCATION CALLED")
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Log.i("MainActivity", "INSIDE")
                Toast.makeText(context, "Location Updated", Toast.LENGTH_SHORT).show()
                latitude = location.latitude.toString()
                longitude = location.longitude.toString()
                Log.i("MainActivity", latitude)
                Log.i("MainActivity", longitude)
            }
        }
    }

    private fun checkpermission(permissionArray: Array<String>, context: Context): Boolean {
        var allsuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(
                    context,
                    permissionArray[i]
                ) == PermissionChecker.PERMISSION_DENIED
            )
                allsuccess = false
        }
        return allsuccess
    }

    private fun SubmitAction() {
        LocationInput = binding.locationId.text.toString() //Input of location from user
        Desc = binding.shortId.text.toString() //Input of description from user
        if (Desc.isEmpty() || binding.showImage.drawable == null || latitude == "" || longitude == "") {
            if (Desc.isEmpty()) {
                binding.shortId.error = "Description is required"
                binding.shortId.requestFocus()
            }
            if (binding.showImage.drawable == null) {
                Toast.makeText(parentFragment?.context, "Image is Required", Toast.LENGTH_SHORT)
                    .show()
            }
            if (longitude == "" || latitude == "") {
                Toast.makeText(context, "Use GPS", Toast.LENGTH_SHORT).show()
            }
            return
        }

        binding.progressBar2.visibility = View.VISIBLE
        binding.loadingText.visibility = View.VISIBLE
        binding.locationId.text.clear()
        val color: Int = Color.parseColor("#9CF8EEEE")
        binding.relativeId.setBackgroundColor(color)
        binding.descId.editText!!.text.clear()
        sendimage()
    }

    private fun sendimage() {
        refdata = databaseReference.getReference("FIRST")
        id = refdata.push().key!!
        val refstore = storageReference.getReference("FIRST").child(id)
        val baos = ByteArrayOutputStream()
        imagebit.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadtask = refstore.putBytes(data)
        uploadtask.addOnSuccessListener {
            refstore.downloadUrl.addOnSuccessListener {
                Log.i("Check", "Image Uploaded")
                sendata()
            }
        }
    }

    private fun sendata() {
        val data = Data(id, LocationInput, Desc, latitude, longitude)
        refdata.child(id).setValue(data).addOnSuccessListener {
            binding.progressBar2.visibility = View.GONE
            binding.loadingText.visibility = View.VISIBLE
            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_titleFragment_to_displayData)

        }
    }

    private fun clickimage() {
        val imageIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(imageIntent, REQUEST_IMAGE_CAPTURE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val extras = data?.extras
        imagebit = extras?.get("data") as Bitmap
        binding.showImage.setImageBitmap(imagebit)
    }

}

