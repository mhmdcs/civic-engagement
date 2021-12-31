package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.repository.CivicEngagementRepository
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**to absolutely ensure that you make one instance of the repository and database for the application, start Koin in an Application class*/
class CivicEngagementApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        /** use Koin Library as a service locator*/

        val module = module {
            //Declare viewModel - to be later inject into Fragment using by inject()
            viewModel { ElectionsViewModel(get(), get()) }
            viewModel { RepresentativeViewModel(get(), get()) }
            viewModel { VoterInfoViewModel(get(), get()) }

            //Declare repository and database to be singletons so that they can be used across multiple files
            single { CivicEngagementRepository(get()) }
            single { ElectionDatabase.getInstance(this@CivicEngagementApplication).electionDao }
        }

        startKoin {
            androidContext(this@CivicEngagementApplication)
            modules(listOf(module))
        }
    }
}