package be.vives.pronostiekappwouthostens.pronstiek.a.resultaat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import be.vives.pronostiekappwouthostens.databinding.MatchInPronostiekAanmakenBinding
import be.vives.pronostiekappwouthostens.databinding.PronostiekInPronostiekOverzichtBinding
import be.vives.pronostiekappwouthostens.databinding.PronostiekInPronostiekOverzichtresBinding
import be.vives.pronostiekappwouthostens.databinding.PronostiekResultaatOverzichtFragmentBinding

class PronostiekAdapterRes(val clickListener: be.vives.pronostiekappwouthostens.pronstiek.a.resultaat.PronostiekClickListener): ListAdapter<Pronostiek, PronostiekAdapterRes.ViewHolder>(PronostiekDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: PronostiekInPronostiekOverzichtresBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(
            pronostiek: Pronostiek,
            clickListener: PronostiekClickListener
        ) {
            binding.pronostieken = pronostiek
            binding.clickListener = clickListener
//            if(matches.isselected)
//            {
//                binding.cellmatchen.setBackgroundColor(Color.parseColor("#808080"))
//            }
//            else
//            {
//                binding.cellmatchen.setBackgroundColor(Color.parseColor("#FFFFFF"))
//            }
            binding.invalidateAll()
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PronostiekInPronostiekOverzichtresBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
class PronostiekDiffCallback : DiffUtil.ItemCallback<Pronostiek>() {

    override fun areItemsTheSame(oldItem: Pronostiek, newItem: Pronostiek): Boolean {
        return oldItem.naam == newItem.naam && oldItem.GroepID == newItem.GroepID
    }

    override fun areContentsTheSame(oldItem: Pronostiek, newItem: Pronostiek): Boolean {
        return oldItem == newItem
    }
}

class PronostiekClickListener(val clickListener: (pronostiek:Pronostiek, view: View) -> Unit) {
    fun onClick(pronostiek: Pronostiek, view: View) = clickListener(pronostiek,view)
}