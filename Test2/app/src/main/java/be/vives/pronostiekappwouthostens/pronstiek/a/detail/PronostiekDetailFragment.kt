package be.vives.pronostiekappwouthostens.pronstiek.a.detail

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
import be.vives.pronostiekappwouthostens.databinding.PronostiekDetailFragmentBinding
import com.google.firebase.auth.FirebaseUser

class PronostiekDetailFragment : Fragment() {

    private lateinit var viewModel: PronostiekDetailViewModel
    private lateinit var binding: PronostiekDetailFragmentBinding
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.pronostiek_detail_fragment, container, false)
        user = (activity as MainActivity).user!!
        val args = PronostiekDetailFragmentArgs.fromBundle(requireArguments())
        val fact = PronostiekDetailViewModelFactory(user, args.pronostiek, args.groep)
        viewModel = ViewModelProvider(this,fact).get(PronostiekDetailViewModel(user,args.pronostiek,args.groep)::class.java)

        val adapter = MatchesUserPronostiekAdapter(MatchesUserPronostiekClickListener{ matches: Matches, view: View ->
            })
        viewModel.matchesAvailable.observe(viewLifecycleOwner, Observer {
            if (it) {
                adapter.submitList(viewModel.matchesList.value)
                binding.pronostiekUserRecyclerview.invalidate()
                binding.textView16.text="Uw Score voor deze pronostiek is " + viewModel.score.value +" /10 !"
                viewModel.listRefreshed()
            }
        })
        binding.pronostiekUserRecyclerview.adapter = adapter
        binding.setLifecycleOwner(this)
        val manager = LinearLayoutManager(activity)
        binding.pronostiekUserRecyclerview.layoutManager = manager
        return binding.root
    }


}