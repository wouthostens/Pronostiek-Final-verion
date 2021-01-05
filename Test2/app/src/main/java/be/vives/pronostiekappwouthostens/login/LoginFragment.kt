package be.vives.pronostiekappwouthostens.login

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
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
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import be.vives.pronostiekappwouthostens.databinding.LoginFragmentBinding
import be.vives.pronostiekappwouthostens.utils.MyAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.Date
import com.google.type.DateTime
import java.text.SimpleDateFormat

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding : LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?    ): View? {
        val fact = LoginViewModelFactory(MyAuth((requireActivity())))
        viewModel = ViewModelProviders.of(this,fact).get(LoginViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater,R.layout.login_fragment, container, false)
        binding.myModel = viewModel
        var groep = Groep("leeg","leeg")
        var pronostiek = Pronostiek("","",null,"")
        //te verwijderen - testknop pronostiek
//        binding.btnTestPronostiek.setOnClickListener {
//            requireView().findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPronostiekDetail(groep,pronostiek))
//        }

        viewModel.navigateToHomeScree.observe(viewLifecycleOwner , Observer {
            if(it)
            {
                 navigateToHomeScreen()
            }
        })
        viewModel.naviagteToResetPassword.observe(viewLifecycleOwner , Observer {
            if(it)
            {
                navigateToPasswordResetScreen()

            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty() == false)
            {
                Toast.makeText(this.context, it, Toast.LENGTH_LONG).show()
                viewModel.errorfinished()
            }
        })
        viewModel.navigateToRegistreren.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                navigateToRegistreren()
            }
        })
        viewModel.emailsend.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                Toast.makeText(this.context,"Email verstuurd voor nieuw paswoord",Toast.LENGTH_LONG ).show()
                viewModel.emailsendfinished()
            }
        })
        binding.setLifecycleOwner { lifecycle }
        return binding.root
    }

    private fun navigateToRegistreren() {
        requireView().findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrerenFragment2())
        viewModel.navigateToRegistrerenFinished()
    }

    fun navigateToPasswordResetScreen()
    {
        //var user = viewModel.user.value!!
        //requireView().findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToResetUserPasswordFragment(user))
        viewModel.navigateToResetPasswordFinisshed()
    }
    fun navigateToHomeScreen()
    {
        val user = viewModel.user
        Toast.makeText(this.context, "Geldige gebruiker welkom.", Toast.LENGTH_SHORT).show()
        (activity as MainActivity).user = user.value
        requireView().findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment(user.value!!))
        viewModel.navigateToHomeScreenFinisshed()
    }

}