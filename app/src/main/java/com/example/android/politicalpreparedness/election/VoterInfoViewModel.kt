package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.Result
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.CivicEngagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoterInfoViewModel(app: Application, private val repository: CivicEngagementRepository): AndroidViewModel(app) {

    //Add live data to hold voter info
    private val _voterInfoResult = MutableLiveData<Result<VoterInfoResponse>>()
    val voterInfoResult: MutableLiveData<Result<VoterInfoResponse>>
        get() = _voterInfoResult

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: MutableLiveData<VoterInfoResponse>
        get() = _voterInfo

    //Add var and methods to populate voter info
    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election


    //Add var and methods to support loading URLs
    fun getVoterInfo(election: Election) {
        viewModelScope.launch {
            try {
                _voterInfoResult.postValue(Result.Loading)
                _voterInfoResult.postValue(Result.Success(repository.getVoterInfo(election.name, election.id)))
            } catch (exception: Exception) {
                _voterInfoResult.postValue(Result.Error(exception.message ?: ""))
            }
        }
    }

    fun setElection(election: Election) {
        viewModelScope.launch(Dispatchers.IO) {
           // val election = repository.getElectionById(election.id)
            _election.postValue(repository.getElectionById(election.id))
        }
    }



    //Add var and methods to save and remove elections to local database
    //cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    fun updateElection() {
        val election = _election.value ?: return
        election.isFollowed = !election.isFollowed
        viewModelScope.launch {
            repository.updateElection(election)
            _election.value = election
        }
    }

}