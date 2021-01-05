package be.vives.pronostiekappwouthostens.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.pronostiekappwouthostens.resetpwd.ResetUserPasswordViewModel
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException

class WelcomeViewModelFactory (private val user: FirebaseUser) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(WelcomeViewModel::class.java))
            {
                return WelcomeViewModel(user) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
