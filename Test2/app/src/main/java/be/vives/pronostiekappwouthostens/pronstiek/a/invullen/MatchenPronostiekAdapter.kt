package be.vives.pronostiekappwouthostens.pronstiek.a.invullen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import be.vives.pronostiekappwouthostens.databinding.MatchInPronostiekAanmakenBinding
import be.vives.pronostiekappwouthostens.databinding.MatchenInPronostiekInvullenBinding

class MatchenPronostiekAdapter(val clickListener: MatchesPronostiekClickListener): ListAdapter<Matches, MatchenPronostiekAdapter.ViewHolder>(MatchesPronsotiekDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: MatchenInPronostiekInvullenBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(
            matches: Matches,
            clickListener: MatchesPronostiekClickListener
        ) {
            binding.matches = matches
            binding.clickListener = clickListener
            //itembinding.clickListener = clickListener
            if(matches.pronostiekresultaat=="")
            {
                matches.pronostiekresultaat="x"
            }
            if(matches.pronostiekresultaat =="1")
            {
                binding.cellmatchen.setBackgroundColor(Color.parseColor("#81F770"))
            }
            else if (matches.pronostiekresultaat=="2")
            {
                binding.cellmatchen.setBackgroundColor(Color.parseColor("#FB6969"))
            }
            else if (matches.pronostiekresultaat=="x")
            {
                binding.cellmatchen.setBackgroundColor(Color.parseColor("#6D9BFF"))
            }
            binding.invalidateAll()
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MatchenInPronostiekInvullenBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
class MatchesPronsotiekDiffCallback : DiffUtil.ItemCallback<Matches>() {

    override fun areItemsTheSame(oldItem: Matches, newItem: Matches): Boolean {
        return oldItem.matchID == newItem.matchID //&& oldItem.isselected == newItem.isselected
    }

    override fun areContentsTheSame(oldItem: Matches, newItem: Matches): Boolean {
        return oldItem == newItem
    }
}

class MatchesPronostiekClickListener(val clickListener: (matches: Matches, view: View) -> Unit) {
    fun onClick(matches: Matches, view: View) = clickListener(matches,view)
}