package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback) {

    //Create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent, clickListener)
    }

    //Bind the ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

//Create ElectionViewHolder
class ElectionViewHolder private constructor(
    private val binding: ItemElectionBinding,
    private val clickListener: ElectionListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(election: Election) {
        binding.election = election
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    //Add companion object to inflate ViewHolder (from)
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_election
        fun from(parent: ViewGroup, clickListener: ElectionListener): ElectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemElectionBinding.inflate(layoutInflater, parent, false)

            return ElectionViewHolder(binding, clickListener)
        }
    }
}

//Create ElectionDiffCallback
object ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Election, newItem: Election) = oldItem == newItem
}

//Create ElectionListener
fun interface ElectionListener {
    fun onClick(election: Election)
}



