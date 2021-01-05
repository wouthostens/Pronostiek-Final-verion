package be.vives.pronostiekappwouthostens.groep.welcome

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import be.vives.pronostiekappwouthostens.MainActivity
import be.vives.pronostiekappwouthostens.R
import be.vives.pronostiekappwouthostens.databinding.GroepWelcomeFragmentBinding
import com.google.firebase.auth.FirebaseUser

class GroepWelcomeFragment : Fragment() {
    private lateinit var viewModel: GroepWelcomeViewModel
    private lateinit var binding: GroepWelcomeFragmentBinding
    private lateinit var user:FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = (activity as MainActivity).user!!
        val args = GroepWelcomeFragmentArgs.fromBundle(requireArguments())
        val fact = GroepWelcomeViewModelFactory(user,args.groep)
        viewModel = ViewModelProvider(this,fact).get(GroepWelcomeViewModel(user,args.groep)::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.groep_welcome_fragment, container, false)
        binding.myModel = viewModel
        viewModel.navigeerPronostiekenOverzicht.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                navigeerToPronostiekOverzicht()
            }
        })
        viewModel.navigateToPronostiekAanmaken.observe(viewLifecycleOwner, Observer {
           if(it)
           {
               navigeertoPronostiekaanmaken()
           }
        })
        viewModel.navigeerPronostiekenResultaten.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                navigeertoPronostiekResultaten()
            }
        })
        return binding.root
    }
    fun  navigeertoPronostiekResultaten()
    {
        requireView().findNavController().navigate(GroepWelcomeFragmentDirections.actionGroepWelcomeFragmentToPronostiekResultaatOverzichtFragment(viewModel.groep.value!!))
        viewModel.navigeerPronostiekenOverzichtFinished()
    }
    fun navigeerToPronostiekOverzicht()
    {
        requireView().findNavController().navigate(GroepWelcomeFragmentDirections.actionGroepWelcomeFragmentToPronostiekenOverzichtFragment(viewModel.groep.value!!))
        viewModel.navigeerPronostiekenOverzichtFinished()
    }
    fun navigeertoPronostiekaanmaken()
    {
        requireView().findNavController().navigate(GroepWelcomeFragmentDirections.actionGroepWelcomeFragmentToPronostiekAanmakenFragment(viewModel.groep.value!!))
        viewModel.navigeerPronostiekAanmakenFinished()
    }

}