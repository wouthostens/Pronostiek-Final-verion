package be.vives.pronostiekappwouthostens.pronstiek.a.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import be.vives.pronostiekappwouthostens.pronstiek.a.invullen.PronostiekInvullenViewModel
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException

class PronostiekDetailViewModelFactory(private val user: FirebaseUser, private val pronostiek: Pronostiek, private val groep: Groep) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PronostiekDetailViewModel::class.java))
        {
            return PronostiekDetailViewModel(user,pronostiek,groep) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}