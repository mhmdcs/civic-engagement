package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.repository.CivicEngagementRepository

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(app: Application, private val repository: CivicEngagementRepository): AndroidViewModel(app) {

    //TODO: Create live data val for upcoming elections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}