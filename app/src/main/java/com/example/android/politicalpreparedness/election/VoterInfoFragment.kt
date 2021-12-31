package com.example.android.politicalpreparedness.election

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import org.koin.android.ext.android.inject

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding
    val viewModel: VoterInfoViewModel by inject()

    /**Hint: You will need to ensure proper data is provided from previous fragment.*/
    private val args: VoterInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVoterInfoBinding.inflate(inflater)

        //Add ViewModel values and create ViewModel
        viewModel.setElection(args.election)
        viewModel.getVoterInfo(args.election)

        //Add binding values
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //Handle save button UI state
        //cont'd Handle save button clicks
        viewModel.election.observe(viewLifecycleOwner) {
            binding.voterInfoFollowElectionButton.text =
                if (!it.isFollowed) getString(R.string.voterinfo_follow_election) else getString(R.string.voterinfo_unfollow_election)
        }

        //Populate voter info -- hide views without provided data.
        viewModel.voterInfo.observe(viewLifecycleOwner, { voterInfoResponse ->
            val adminBody = voterInfoResponse.state?.firstOrNull()?.electionAdministrationBody

            //Handle loading of URLs
            binding.voterInfoVotingLocationsTextview.setOnClickListener {
                loadURL(adminBody?.votingLocationFinderUrl)
            }

            binding.ballotInformationTextview.setOnClickListener {
                loadURL(adminBody?.ballotInfoUrl)
            }
        })

        return binding.root

    }

    //Create method to load URL intents
    @SuppressLint("QueryPermissionsNeeded")
    private fun loadURL(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }


}