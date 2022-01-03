package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class CivicEngagementRepository(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
     val electionsFollowed: LiveData<List<Election>>  = electionDao.getFollowedElections()
     val electionsUpcoming: LiveData<List<Election>> = electionDao.getAllElections()

    suspend fun getRepresentatives(address: Address) = withContext(ioDispatcher) {
        try {
            CivicsApi.retrofitService.getRepresentatives(address.zip)
        }
    catch (error: Exception){
        error.printStackTrace()
        Log.i("Repository", "Fetch data Error $error")
    }
    }

    suspend fun refreshElectionsData() = withContext(ioDispatcher) {
        try {
            val response = CivicsApi.retrofitService.getUpcomingElections()
            Log.i("Repository", "Is the list of products empty? $response")
            val elections = response.elections
            electionDao.insertAll(*elections.toTypedArray())
            electionsFollowed
        }
        catch (error: Exception){
            error.printStackTrace()
            Log.i("Repository", "Fetch data Error $error")
        }
    }

    suspend fun updateElection(election: Election) = withContext(ioDispatcher) {
        electionDao.updateElection(election)
    }

    suspend fun getElectionById(id: Int): Election {
        return electionDao.getElectionById(id)
    }

    suspend fun getVoterInfo(address: String, electionId: Int) = withContext(ioDispatcher) {
        try {
        CivicsApi.retrofitService.getVoterInfo(address, electionId)
        }
        catch (error: Exception){
            error.printStackTrace()
            Log.i("Repository", "Fetch data Error $error")
        }
    }

    suspend fun deleteElection(election: Election) = withContext(ioDispatcher) {
        electionDao.deleteElection(election)
    }

    suspend fun clear() = withContext(ioDispatcher) {
        electionDao.clear()
    }

}