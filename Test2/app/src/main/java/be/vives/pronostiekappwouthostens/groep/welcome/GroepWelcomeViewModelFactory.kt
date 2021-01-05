package be.vives.pronostiekappwouthostens.groep.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.pronostiekappwouthostens.classes.Groep
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException

class GroepWelcomeViewModelFactory(private val user: FirebaseUser, private val groep:Groep) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GroepWelcomeViewModel::class.java))
        {
            return GroepWelcomeViewModel(user,groep) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}