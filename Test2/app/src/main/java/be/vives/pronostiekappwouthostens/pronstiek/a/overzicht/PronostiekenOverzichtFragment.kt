package be.vives.pronostiekappwouthostens.pronstiek.a.overzicht

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.pronostiekappwouthostens.MainActivity
import be.vives.pronostiekappwouthostens.R
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import be.vives.pronostiekappwouthostens.databinding.PronostiekenOverzichtFragmentBinding
import be.vives.pronostiekappwouthostens.login.LoginFragmentDirections
import be.vives.pronostiekappwouthostens.pronstiek.a.MatcheAdapter
import be.vives.pronostiekappwouthostens.pronstiek.a.MatchesClickListener
import be.vives.pronostiekappwouthostens.pronstiek.a.overzicht.PronostiekenOverzichtFragmentDirections.actionPronostiekenOverzichtFragmentToPronostiekInvullenFragment
import com.google.firebase.auth.FirebaseUser

class PronostiekenOverzichtFragment : Fragment() {
    private lateinit var viewModel: PronostiekenOverzichtViewModel
    private lateinit var user: FirebaseUser
    private lateinit var binding: PronostiekenOverzichtFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = (activity as MainActivity).user!!
        val args = PronostiekenOverzichtFragmentArgs.fromBundle(requireArguments())
        val fact = PronostiekOverzichtViewmodelFactory(user,args.groep)
        viewModel = ViewModelProvider(this,fact).get(PronostiekenOverzichtViewModel(user,args.groep)::class.java)
        binding =  DataBindingUtil.inflate(inflater,R.layout.pronostieken_overzicht_fragment, container, false)
        binding.myModel=viewModel
        val adapter = PronostiekAdapter(PronostiekClickListener { pronostiek:Pronostiek, view: View ->
            viewModel.pronostiekClicked(pronostiek,view) })
        viewModel.pronstiekenBeschikbaar.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                adapter.submitList(viewModel.pronstieken.value!!.toList())
                viewModel.pronostiekenloadenFinished()
            }
        })
        viewModel.navigatToPronostiekInvullen.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                requireView().findNavController().navigate(PronostiekenOverzichtFragmentDirections.actionPronostiekenOverzichtFragmentToPronostiekInvullenFragment( viewModel.geselecteerdePronostiek.value!!, viewModel.groep.value!!))
                viewModel.navigeerPronostiekInvullenFinished()
            }
        })
        binding.pronostiekenrecyclerview.adapter = adapter
        binding.setLifecycleOwner(this)
        val manager = LinearLayoutManager(activity)
        binding.pronostiekenrecyclerview.layoutManager = manager

        return binding.root
    }
}