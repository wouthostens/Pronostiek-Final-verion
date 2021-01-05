package be.vives.pronostiekappwouthostens.groep.aansluiten

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.pronostiekappwouthostens.groep.aan.GroepAanViewModel
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException

class GroepAansluitenViewModelFactory (private val user: FirebaseUser) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GroepAansluitenViewModel::class.java))
        {
            return GroepAansluitenViewModel(user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}