package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.CivicEngagementRepository
import com.example.android.politicalpreparedness.utils.NavigationCommand
import com.example.android.politicalpreparedness.utils.SingleLiveEvent
import kotlinx.coroutines.launch

//Construct ViewModel and provide election datasource
class ElectionsViewModel(app: Application, private val repository: CivicEngagementRepository): AndroidViewModel(app) {

    val navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()


    //Create live data val for upcoming elections
    val upcomingElections: LiveData<List<Election>>
        get() = repository.electionsUpcoming

    //Create live data val for saved elections
    val followedElections: LiveData<List<Election>>
        get() = repository.electionsFollowed

    //Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun refreshElections() {
        viewModelScope.launch {
            repository.refreshElectionsData()
        }
    }

    //navigate to VoterInfo Fragment
    fun navigateToVoterInfo(election: Election) {
        navigationCommand.value = NavigationCommand.To(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election)
        )
    }

    init {
        refreshElections()
    }

}

