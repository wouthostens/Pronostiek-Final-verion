package be.vives.pronostiekappwouthostens.groep.aansluiten

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import be.vives.pronostiekappwouthostens.MainActivity
import be.vives.pronostiekappwouthostens.R
import be.vives.pronostiekappwouthostens.databinding.GroepAansluitenFragmentBinding
import be.vives.pronostiekappwouthostens.groep.aan.GroepAanViewModelFactory
import com.google.firebase.auth.FirebaseUser

class GroepAansluitenFragment : Fragment() {
    private lateinit var viewModel: GroepAansluitenViewModel
    private lateinit var binding: GroepAansluitenFragmentBinding
    private lateinit var  user : FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = (activity as MainActivity).user!!
        val fact = GroepAansluitenViewModelFactory(user)
        viewModel = ViewModelProvider(this,fact).get(GroepAansluitenViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater,R.layout.groep_aansluiten_fragment, container, false)
        binding.myModel = viewModel
        binding.setLifecycleOwner { lifecycle }

        viewModel.navigateToHomeScree.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                requireView().findNavController().navigate(GroepAansluitenFragmentDirections.actionGroepAansluitenFragment2ToWelcomeFragment(user))
                viewModel.navigateToHomeScreenFinished()
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty() == false)
            {
                Toast.makeText(this.context, it, Toast.LENGTH_LONG).show()
                viewModel.errorfinished()
            }
        })

        return binding.root
    }
}