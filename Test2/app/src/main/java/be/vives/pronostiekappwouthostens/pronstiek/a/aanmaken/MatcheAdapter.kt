package be.vives.pronostiekappwouthostens.pronstiek.a

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.Converters
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.vives.pronostiekappwouthostens.R
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.databinding.GroepInListBinding
import be.vives.pronostiekappwouthostens.databinding.MatchInPronostiekAanmakenBinding
import be.vives.pronostiekappwouthostens.welcome.GroepenAdapter

class MatcheAdapter(val clickListener: MatchesClickListener): ListAdapter<Matches, MatcheAdapter.ViewHolder>(MatchesDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: MatchInPronostiekAanmakenBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(
            matches: Matches,
            clickListener: MatchesClickListener
        ) {
            binding.matches = matches
            binding.clickListener = clickListener
           //itembinding.clickListener = clickListener
            if(matches.isselected)
            {
                binding.cellmatchen.setBackgroundColor(Color.parseColor("#808080"))
            }
            else
            {
                binding.cellmatchen.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            binding.invalidateAll()
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MatchInPronostiekAanmakenBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
class MatchesDiffCallback : DiffUtil.ItemCallback<Matches>() {

    override fun areItemsTheSame(oldItem: Matches, newItem: Matches): Boolean {
        return oldItem.matchID == newItem.matchID && oldItem.isselected == newItem.isselected
    }

    override fun areContentsTheSame(oldItem: Matches, newItem: Matches): Boolean {
        return oldItem == newItem
    }
}

class MatchesClickListener(val clickListener: (matches: Matches, view:View) -> Unit) {
    fun onClick(matches: Matches, view: View) = clickListener(matches,view)
}