package be.vives.pronostiekappwouthostens.groep.aan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.pronostiekappwouthostens.login.LoginViewModel
import be.vives.pronostiekappwouthostens.utils.MyAuth
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException

class GroepAanViewModelFactory  (private val user: FirebaseUser) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GroepAanViewModel::class.java))
        {
            return GroepAanViewModel(user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}