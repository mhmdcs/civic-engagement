package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

//Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .add(ElectionAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(CivicsHttpClient.getClient())
        .baseUrl(BASE_URL)
        .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {

    //Add elections API Call https://www.googleapis.com/civicinfo/v2/elections?key=[YOUR_APY_KEY]
    @GET("elections") //endpoint
    suspend fun getUpcomingElections(): ElectionResponse

    //Add voterinfo API Call https://civicinfo.googleapis.com/civicinfo/v2/voterinfo?address=[SOME_ADDRESS]]&electionId=[SOME_ELECTION_ID]&key=[YOUR_APY_KEY]
    @GET("voterinfo") //endpoint
    suspend fun getVoterInfo(
        @Query("address") address: String, //query
        @Query("electionId") electionId: Int //query
    ): VoterInfoResponse

    //Add representatives API Call https://www.googleapis.com/civicinfo/v2/representatives?key=[YOUR_APY_KEY]&address=[SOME_ADDRESS]
    @GET("representatives") //endpoint
    suspend fun getRepresentatives(@Query("address") address: String): RepresentativeResponse //query

}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}

//sample for voterinfo GET request method
//https://civicinfo.googleapis.com/civicinfo/v2/voterinfo?address=1263%20Pacific%20Ave.%20Kansas%20City%20KS&electionId=2000&key=AIzaSyDDqCD2-unyH0-yHLiGEkHSPxsbmgOeV5s

//sample for elections GET request method
//https://civicinfo.googleapis.com/civicinfo/v2/elections?key=AIzaSyDDqCD2-unyH0-yHLiGEkHSPxsbmgOeV5s

//sample for representatives GET request method
//https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyDDqCD2-unyH0-yHLiGEkHSPxsbmgOeV5s&address=1263%20Pacific%20Ave.%20Kansas%20City%20KS
