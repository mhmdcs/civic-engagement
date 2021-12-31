package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {
    //Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg election: Election)

    //Add select all election query
    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>>

    //Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElectionById(id: Int): Election

    //Add delete query
    @Delete
    suspend fun deleteElection(election: Election)

    //Add clear query
    @Query("DELETE FROM election_table")
    suspend fun clear()

    @Update
    suspend fun updateElection(election: Election)

    @Query("SELECT * FROM election_table WHERE isFollowed = 1")
    fun getFollowedElections(): LiveData<List<Election>>

}