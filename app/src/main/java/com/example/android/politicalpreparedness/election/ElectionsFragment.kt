package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.utils.NavigationCommand
import org.koin.android.ext.android.inject

class ElectionsFragment : Fragment() {

    private lateinit var binding: FragmentElectionBinding

    //Add ViewModel values and create ViewModel
    val viewModel: ElectionsViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentElectionBinding.inflate(inflater)

        binding = FragmentElectionBinding.inflate(inflater, container, false)

        //Added binding values
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        //TODO: Link elections to voter info

        //Initiate upcomingElectionsAdapter recycler adapter
        val upcomingElectionsAdapter = ElectionListAdapter {
            viewModel.navigateToVoterInfo(it)
        }

        //Initiate followedElectionsAdapter recycler adapter
        val followedElectionsAdapter = ElectionListAdapter {
            viewModel.navigateToVoterInfo(it)
        }

        //Populate both recycler adapters
        binding.electionUpcomingElectionsRecyclerview.adapter = upcomingElectionsAdapter
        binding.electionFollowedElectionsRecyclerview.adapter = followedElectionsAdapter

        //Refresh adapters when fragment loads
        viewModel.upcomingElections.observe(viewLifecycleOwner, { elections ->
            upcomingElectionsAdapter.submitList(elections)
        })

        viewModel.followedElections.observe(viewLifecycleOwner, { elections ->
            followedElectionsAdapter.submitList(elections)
        })


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.navigationCommand.observe(this) { command ->
            when (command) {
                is NavigationCommand.To -> findNavController().navigate(command.directions)
                is NavigationCommand.Back -> findNavController().popBackStack()
                is NavigationCommand.BackTo -> findNavController().popBackStack(
                    command.destinationId,
                    false
                )
            }
        }
    }


}