package be.vives.pronostiekappwouthostens.pronstiek.a.aanmaken

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.pronostiekappwouthostens.MainActivity
import be.vives.pronostiekappwouthostens.R
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.databinding.PronostiekAanmakenFragmentBinding
import be.vives.pronostiekappwouthostens.pronstiek.a.MatcheAdapter
import be.vives.pronostiekappwouthostens.pronstiek.a.MatchesClickListener
import com.google.firebase.auth.FirebaseUser

class PronostiekAanmakenFragment : Fragment() {
    private lateinit var viewModel: PronostiekAanmakenViewModel
    private lateinit var binding: PronostiekAanmakenFragmentBinding
    private lateinit var user:FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = (activity as MainActivity).user!!
        val args = PronostiekAanmakenFragmentArgs.fromBundle(requireArguments())
        val fact = PronostiekAanmakenViewmodelFactory(user,args.groep)
        viewModel = ViewModelProvider(this, fact).get(PronostiekAanmakenViewModel(user,args.groep)::class.java)
        binding =  DataBindingUtil.inflate(inflater,R.layout.pronostiek_aanmaken_fragment, container, false)
        binding.myModel=viewModel
        val adapter = MatcheAdapter(MatchesClickListener { matches: Matches, view: View ->
            viewModel.matchClicked(matches,view) })
        viewModel.matchesBeschikbaar.observe(viewLifecycleOwner, Observer {
            if(it){
                adapter.submitList(viewModel.matches.value!!.toList())
                viewModel.matchloadenFinished()
            }
        })
        viewModel.changed.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                binding.aantalgeselecteerdematcehn.text = (viewModel.aantalselecteerde.value!!.size +1).toString() + "/10 matchen geselecteerd"
                adapter.notifyDataSetChanged()
                viewModel.changedfinished()
            }
        })
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty())
            {
                binding.invalidateAll()
            }
        })

        viewModel.pronostiekklar.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                binding.buttonPronostiekOpslaan.isEnabled = true
                Toast.makeText(this.context, viewModel.bericht.value, Toast.LENGTH_LONG).show()
                //viewModel.pronstiekklaarfinished()
                viewModel.berichtfinshed()
            }
            else
            {
                binding.buttonPronostiekOpslaan.isEnabled = false
            }
        })
        viewModel.bericht.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty())
            {
                Toast.makeText(this.context, viewModel.bericht.value, Toast.LENGTH_LONG).show()
                viewModel.berichtfinshed()
            }

        })

        binding.pronostiekenrecyclerview.adapter = adapter
        binding.setLifecycleOwner(this)
        val manager = LinearLayoutManager(activity)
        binding.pronostiekenrecyclerview.layoutManager = manager


        return binding.root
    }

}