package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.CivicEngagementRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(app: Application, private val repository: CivicEngagementRepository) :
    AndroidViewModel(app) {


    //Establish live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    init {
        addressLine1.value = ""
        addressLine2.value = ""
        city.value = ""
        state.value = ""
        zip.value = ""

        _address.value = Address("", "", "", "", "")
    }


    //Create function to fetch representatives from API from a provided address
    fun getRepresentativesByAddress(address: Address?) {
        viewModelScope.launch {
            _representatives.value = arrayListOf()
            if (address != null) {
                try {
                    _address.value = address
                    val (offices, officials) = repository.getRepresentatives(address)
                    _representatives.value =
                        offices.flatMap { office -> office.getRepresentatives(officials) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun loadRepresentatives() {
        viewModelScope.launch {
            getRepresentativesByAddress(
                Address(
                    addressLine1.value!!,
                    addressLine2.value!!,
                    city.value!!,
                    state.value!!,
                    zip.value!!
                )
            )
        }
    }


    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //Created function get address from geo location

    //Created function to get address from individual fields


}
