package be.vives.pronostiekappwouthostens.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.databinding.GroepInListBinding

class GroepenAdapter (val clickListener: GroepClickListener): ListAdapter<Groep, GroepenAdapter.ViewHolder>(GroepDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: GroepInListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(
            groep: Groep,
            clickListener: GroepClickListener
        ) {
            binding.groep = groep
            binding.clickListener = clickListener
            //itembinding.clickListener = clickListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GroepInListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
class GroepDiffCallback : DiffUtil.ItemCallback<Groep>() {

    override fun areItemsTheSame(oldItem: Groep, newItem: Groep): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Groep, newItem: Groep): Boolean {
        return oldItem == newItem
    }
}

class GroepClickListener(val clickListener: (groep: Groep) -> Unit) {
    fun onClick(groep: Groep) = clickListener(groep)
}