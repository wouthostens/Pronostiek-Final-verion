package be.vives.pronostiekappwouthostens.pronstiek.a.overzicht

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.pronostiekappwouthostens.classes.Groep
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException

class PronostiekOverzichtViewmodelFactory (private val user: FirebaseUser, private val groep: Groep) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PronostiekenOverzichtViewModel::class.java))
        {
            return PronostiekenOverzichtViewModel(user,groep) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}