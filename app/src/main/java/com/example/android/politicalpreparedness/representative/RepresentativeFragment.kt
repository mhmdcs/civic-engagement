package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.representative.model.Representative
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject
import java.util.Locale

class RepresentativeFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var representativesListAdapter: RepresentativeListAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //Declare ViewModel
    val viewModel: RepresentativeViewModel by inject()

    companion object {
        //Add Constant for Location request
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentRepresentativeBinding.inflate(inflater)
        //Establish bindings
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            representativesListAdapter = RepresentativeListAdapter(RepresentativeListener {})
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.states,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.representativeDropdownlistState.adapter = adapter
        }

        binding.representativeMyRepresentativesRecyclerview.adapter = representativesListAdapter

        viewModel.representatives.observe(
            viewLifecycleOwner,
            Observer<List<Representative>> { representatives ->
                representativesListAdapter.submitList(representatives)
            })

        binding.representativeFindMyRepresentativesManualButton.setOnClickListener {
            hideKeyboard()
            viewModel.refreshRepresentatives()
        }

        binding.representativeFindMyRepresentativesMyLocationButton.setOnClickListener {
            hideKeyboard()
            getLocation()
            Log.i("RepresentativeFragment","LocationButton clicked")
        }

        return binding.root


        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search

    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        //Handle location permission result to get location on permission granted
//
//        if (REQUEST_LOCATION_PERMISSIONS_CODE == requestCode) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLocation()
//            }
//        }
//
//    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        // Check if location permissions are granted and if so enable the location data layer.
        if (requestCode == REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE && grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            getLocation()
        }
    }


    private fun isPermissionGranted(): Boolean {
        return ContextCompat
            .checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if(isPermissionGranted()){
            Log.i("RepresentativeFragment","isPermissionGranted passed "+isPermissionGranted())

            //Get location from LocationServices
            fusedLocationProviderClient.requestLocationUpdates(null,null)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
                Log.i("RepresentativeFragment","get lastLocation succeeded")
                Log.i("RepresentativeFragment","get lastLocation value is " +location)
                if (location != null) {
                    viewModel.getRepresentativesByAddress(geoCodeLocation(location))
                }
            }.addOnFailureListener {
                Log.i("RepresentativeFragment","get lastLocation failed")
            }
        }
        else {
            val permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            val resultCode = REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            requestPermissions(
                permissionsArray,
                resultCode
            )
        }

    }

//    @SuppressLint("MissingPermission")
//    private fun getLocation() {
//        //Get location from LocationServices
//        val locationResult = fusedLocationProviderClient.lastLocation
//        locationResult?.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val location = task.result
//                val address = location?.geoCodeLocation()
//                address?.let { viewModel.getAddressFromGeoLocation(it) }
//            }
//        }
//    }


//    private fun Location.geoCodeLocation(): Address {
//        val geocode = Geocoder(context, Locale.getDefault())
//        return geocode.getFromLocation(latitude, longitude, 1)
//            .map { address ->
//                Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
//            }
//            .first()
//    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }



}