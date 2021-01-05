package be.vives.pronostiekappwouthostens.pronstiek.a.invullen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException

class PronostiekInvullenViewModelFactory (private val user: FirebaseUser,private val pronostiek:Pronostiek, private val groep: Groep) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PronostiekInvullenViewModel::class.java))
        {
            return PronostiekInvullenViewModel(user,pronostiek,groep) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}