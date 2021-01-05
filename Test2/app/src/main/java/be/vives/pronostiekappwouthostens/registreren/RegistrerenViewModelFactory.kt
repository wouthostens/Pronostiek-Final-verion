package be.vives.pronostiekappwouthostens.registreren

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.pronostiekappwouthostens.login.LoginViewModel
import be.vives.pronostiekappwouthostens.utils.MyAuth
import java.lang.IllegalArgumentException

class RegistrerenViewModelFactory (private val myAuth: MyAuth) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(RegistrerenViewModel::class.java))
            {
                return RegistrerenViewModel(myAuth) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
