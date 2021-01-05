package be.vives.pronostiekappwouthostens.resetpwd

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import be.vives.pronostiekappwouthostens.R
import be.vives.pronostiekappwouthostens.databinding.ResetUserPasswordFragmentBinding

class ResetUserPasswordFragment : Fragment() {
    private lateinit var viewModel: ResetUserPasswordViewModel
    private lateinit var binding: ResetUserPasswordFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.reset_user_password_fragment, container, false)
        val args = ResetUserPasswordFragmentArgs.fromBundle(requireArguments())
        val fact = ResetUserPasswordViewModelFactory(args.user!!)
        viewModel = ViewModelProviders.of(this , fact ).get(ResetUserPasswordViewModel::class.java)
        binding.myModel = viewModel

        return binding.root
    }

    fun opslaanNieuwWachtwoord(password: String)
    {

    }


}