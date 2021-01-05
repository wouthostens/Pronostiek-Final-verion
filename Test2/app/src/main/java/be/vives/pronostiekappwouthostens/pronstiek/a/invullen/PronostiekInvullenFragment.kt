package be.vives.pronostiekappwouthostens.pronstiek.a.invullen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.pronostiekappwouthostens.MainActivity
import be.vives.pronostiekappwouthostens.R
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.databinding.PronostiekInvullenFragmentBinding
import com.google.firebase.auth.FirebaseUser

class PronostiekInvullenFragment : Fragment() {
    private lateinit var viewModel: PronostiekInvullenViewModel
    private lateinit var user:FirebaseUser
    private lateinit var binding: PronostiekInvullenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = (activity as MainActivity).user!!
        val args = PronostiekInvullenFragmentArgs.fromBundle(requireArguments())
        val fact = PronostiekInvullenViewModelFactory(user, args.pronostiek, args.groep)
        viewModel = ViewModelProvider(this, fact).get(PronostiekInvullenViewModel(user,args.pronostiek,args.groep)::class.java)
        binding =  DataBindingUtil.inflate(inflater,R.layout.pronostiek_invullen_fragment,container,false)
        binding.myModel = viewModel
        val adapter = MatchenPronostiekAdapter(MatchesPronostiekClickListener{ matches: Matches, view: View ->
            viewModel.pronostiekClicked(matches,view)})
        binding.textView13.text ="Naam van pronostiek: "+ viewModel.pronstieken.value!!.naam
        viewModel.pronstiekenBeschikbaar.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                adapter.submitList(viewModel.pronstieken.value!!.matchen!!.toList())
                binding.pronostiekenrecyclerview.invalidate()
                viewModel.matchegeladenFinished()
                viewModel.matchenbischikbaar()
            }
        })
        viewModel.matchenbeschikbaar.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                adapter.submitList(viewModel.pronstieken.value!!.matchen!!.toList())
                binding.pronostiekenrecyclerview.invalidate()
                viewModel.matchenbischikbaarfinisheed()
            }
        })
        viewModel.changed.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                binding.aantalgeselecteerdematcehn.text = "groen = thuisploeg wint, rood = uitploeg wint, blauw = gelijk"
                adapter.notifyDataSetChanged()
                viewModel.changedFinished()
            }
        })
        binding.pronostiekenrecyclerview.adapter = adapter
        binding.setLifecycleOwner(this)
        val manager = LinearLayoutManager(activity)
        binding.pronostiekenrecyclerview.layoutManager = manager

        return binding.root
    }

}