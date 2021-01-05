package be.vives.pronostiekappwouthostens.utils

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth

class MyAuth(activity: FragmentActivity) {

    private lateinit var auth: FirebaseAuth
    private lateinit var activity: FragmentActivity

    init {
        this.activity = activity
        auth = FirebaseAuth.getInstance()
    }

    fun getContext(): Activity {
        return activity
    }

    fun getAuth(): FirebaseAuth {
        return auth
    }
}