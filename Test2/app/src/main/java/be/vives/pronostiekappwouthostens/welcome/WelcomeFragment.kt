package be.vives.pronostiekappwouthostens.welcome

import android.graphics.Bitmap
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
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.pronostiekappwouthostens.R
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.databinding.WelcomeFragmentBinding
import be.vives.pronostiekappwouthostens.login.LoginFragmentDirections
import be.vives.pronostiekappwouthostens.login.LoginViewModelFactory
import be.vives.pronostiekappwouthostens.utils.MyAuth

class WelcomeFragment : Fragment() {
    private lateinit var viewModel: WelcomeViewModel
    private lateinit var binding: WelcomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = WelcomeFragmentArgs.fromBundle(requireArguments())
        val fact = WelcomeViewModelFactory(args.user)
        viewModel = ViewModelProvider(this,fact).get(WelcomeViewModel(args.user)::class.java)
        binding = DataBindingUtil.inflate(inflater,R.layout.welcome_fragment, container, false)
        binding.myModel = viewModel

        viewModel.navigateToGroopScreen.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                navigateToGroopScreen()
            }
        })
        viewModel.navigatToAansluitenGroopScreen.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                navigateToAansluitenGroepenScreen()
            }
        })
        val adapter = GroepenAdapter(GroepClickListener {
            viewModel.groepClicked(it!!) })

        binding.ProfilePicImageview.setImageURI(null)

        viewModel.fireStoreDBRepository._groepen.observe(viewLifecycleOwner, Observer {
                        if(it.size>0)
            {
                adapter.submitList(viewModel.fireStoreDBRepository._groepen.value!!.toList())
            }
        })
        viewModel.groepen.observe(viewLifecycleOwner, Observer {
            if(it.size>0)
            {
                adapter.submitList(viewModel.groepen.value!!.toList())
            }
        })
        viewModel.bmp.observe(viewLifecycleOwner, Observer {
            if (it !=null)
            {
                binding.ProfilePicImageview.setImageBitmap(Bitmap.createScaledBitmap(viewModel.bmp.value!!, 25, 25, false));
            }
        })
        viewModel.fireStoreDBRepository._groepenBeschikbaar.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                adapter.submitList(viewModel.fireStoreDBRepository._groepen.value!!.toList())
            }
        })
        viewModel.groepenBeschikbaar.observe(viewLifecycleOwner, Observer {
            if (it) {
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.navigatToGroepWelcomeScreen.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                navigateToGroepWelcomeScreen()
            }
        })
        binding.rvGroepen.adapter = adapter
        binding.setLifecycleOwner(this)
        val manager = LinearLayoutManager(activity)
        binding.rvGroepen.layoutManager = manager
        return binding.root
    }
    fun navigateToGroepWelcomeScreen()
    {
        requireView().findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToGroepWelcomeFragment(viewModel.groep.value!!))
    }
    fun navigateToGroopScreen()
    {
        val user = viewModel.user.value
        requireView().findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToGroepAanmakenFragment(user!!))
        viewModel.navigateToGroopScreenFinished()
    }
    fun navigateToAansluitenGroepenScreen()
    {
        requireView().findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToGroepAansluitenFragment2(viewModel.user.value!!))
        viewModel.navigerenaansluitgroepenfinished()
    }
    override fun onResume() {
        super.onResume()
        viewModel.ophalenData()
    }
}