package be.vives.pronostiekappwouthostens.registreren

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import be.vives.pronostiekappwouthostens.MainActivity
import be.vives.pronostiekappwouthostens.R
import be.vives.pronostiekappwouthostens.databinding.RegistrerenFragmentBinding
import be.vives.pronostiekappwouthostens.utils.MyAuth

class RegistrerenFragment : Fragment() {
    private lateinit var viewModel: RegistrerenViewModel
    private lateinit var binding: RegistrerenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?    ): View? {
        val fact = RegistrerenViewModelFactory(MyAuth((requireActivity())))
        viewModel = ViewModelProviders.of(this,fact).get(RegistrerenViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater,R.layout.registreren_fragment, container, false)
        binding.myModel = viewModel
        binding.setLifecycleOwner { lifecycle }
        viewModel.profilepic.value = Uri.parse(R.drawable.images.toString())
        viewModel.navigateToLoginScreen.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                naviagteToLogin()
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty() == false)
            {
                Toast.makeText(this.context, it, Toast.LENGTH_LONG).show()
                viewModel.errorfinished()
            }
        })
        viewModel.afbeeldKiezen.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                (activity as MainActivity).afbeeldingKiezen(this)
                viewModel.Afbeeldinggekozen()
            }
        })
        viewModel.cameraopenen.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                (activity as MainActivity).camera(this)
                viewModel.camereopenefinished()
            }
        })
        return binding.root
    }

    fun setImage(imageUri: Uri?) {
        binding.ProfilePicImageview.setImageURI(imageUri)
        viewModel.profilepic.value = imageUri
    }
    fun naviagteToLogin()
    {
        requireView().findNavController().navigate(RegistrerenFragmentDirections.actionRegistrerenFragmentToLoginFragment2())
        viewModel.navigateToLoginScreenFinished()
    }
}