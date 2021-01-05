package be.vives.pronostiekappwouthostens.api

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

abstract class AuthenticationHelperModel: ViewModel() {
    abstract fun setUser(user: FirebaseUser?, error:String)
}