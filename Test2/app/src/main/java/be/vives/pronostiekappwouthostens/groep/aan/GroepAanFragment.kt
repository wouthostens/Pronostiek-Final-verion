package be.vives.pronostiekappwouthostens.groep.aan

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
import be.vives.pronostiekappwouthostens.databinding.GroepAanmakenFragmentBinding
import be.vives.pronostiekappwouthostens.login.LoginFragmentDirections
import be.vives.pronostiekappwouthostens.welcome.WelcomeFragmentArgs
import be.vives.pronostiekappwouthostens.welcome.WelcomeViewModel
import be.vives.pronostiekappwouthostens.welcome.WelcomeViewModelFactory
import com.google.firebase.auth.FirebaseUser

class GroepAanFragment : Fragment() {
    private lateinit var viewModel: GroepAanViewModel
    private lateinit var binding: GroepAanmakenFragmentBinding
    private lateinit var  user :FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = GroepAanFragmentArgs.fromBundle(requireArguments())
        user = (activity as MainActivity).user!!
        val fact = GroepAanViewModelFactory(user)
        viewModel = ViewModelProvider(this,fact).get(GroepAanViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater,R.layout.groep_aanmaken_fragment, container, false)
        binding.myModel = viewModel
        binding.setLifecycleOwner { lifecycle }
        viewModel.navigateToHomeScree.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                requireView().findNavController().navigate(GroepAanFragmentDirections.actionGroepAanmakenFragmentToWelcomeFragment2(user))
                //parentFragmentManager.popBackStack()
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