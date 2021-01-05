package be.vives.pronostiekappwouthostens.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.pronostiekappwouthostens.classes.User
import be.vives.pronostiekappwouthostens.utils.MyAuth
import java.lang.IllegalArgumentException

class LoginViewModelFactory(private val myAuth: MyAuth) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(LoginViewModel::class.java))
            {
                return LoginViewModel(myAuth) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
