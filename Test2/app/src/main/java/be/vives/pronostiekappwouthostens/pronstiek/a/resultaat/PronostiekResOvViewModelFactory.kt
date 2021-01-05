package be.vives.pronostiekappwouthostens.pronstiek.a.resultaat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.pronstiek.a.overzicht.PronostiekenOverzichtViewModel
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException

class PronostiekResOvViewModelFactory(private val user: FirebaseUser, private val groep: Groep) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PronostiekResultaatOverzichtViewModel::class.java))
        {
            return PronostiekResultaatOverzichtViewModel(user,groep) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}