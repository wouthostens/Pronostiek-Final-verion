package be.vives.pronostiekappwouthostens.pronstiek.a.resultaat

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
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import be.vives.pronostiekappwouthostens.databinding.PronostiekResultaatOverzichtFragmentBinding
import be.vives.pronostiekappwouthostens.pronstiek.a.overzicht.PronostiekClickListener
import be.vives.pronostiekappwouthostens.pronstiek.a.overzicht.PronostiekenOverzichtFragmentDirections
import com.google.firebase.auth.FirebaseUser

class PronostiekResultaatOverzichtFragment : Fragment() {
    private lateinit var viewModel: PronostiekResultaatOverzichtViewModel
    private lateinit var user: FirebaseUser
    private lateinit var binding: PronostiekResultaatOverzichtFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = (activity as MainActivity).user!!
        val args = PronostiekResultaatOverzichtFragmentArgs.fromBundle(requireArguments())
        val fact = PronostiekResOvViewModelFactory(user,args.groep)
        viewModel = ViewModelProvider(this,fact).get(PronostiekResultaatOverzichtViewModel(user,args.groep)::class.java)
        binding = DataBindingUtil.inflate(inflater,R.layout.pronostiek_resultaat_overzicht_fragment,container,false)
        binding.myModel = viewModel
        val adapter = PronostiekAdapterRes(be.vives.pronostiekappwouthostens.pronstiek.a.resultaat.PronostiekClickListener { pronostiek: Pronostiek, view: View ->
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
                requireView().findNavController().navigate(PronostiekResultaatOverzichtFragmentDirections.actionPronostiekResultaatOverzichtFragmentToPronostiekDetail(  viewModel.groep.value!!,viewModel.geselecteerdePronostiek.value!!))
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