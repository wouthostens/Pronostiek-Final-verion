package be.vives.pronostiekappwouthostens.api

import android.app.Activity
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AuthenticationHelper(var context: Activity , var listener:AuthenticationHelperModel) {
    val auth = FirebaseAuth.getInstance()
    fun createUser(email:String, password:String, uri: Uri, gebruikersnaam:String)
    {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateProfielfoto(uri,user!!, gebruikersnaam)
                } else {
                    listener.setUser(null,"Registreren mislukt,paswoord minstens 6 karakters/zeker dat u nieuw bent?/Correcte email?")
                }
    }
    }
        fun updateProfielfoto(uri:Uri, user: FirebaseUser, gebruikersnaam: String)
        {
            val storage = Firebase.storage
            val storageRef = storage.reference
            var error = ""
            val riversRef = storageRef.child("profilepics/"+user!!.email!!)
            var uploadTask = riversRef.putFile(uri)
            uploadTask.addOnFailureListener {
                error="foto uploaden mislukt"
                listener.setUser(user,error)
            }


            var profileUpdates =   UserProfileChangeRequest.Builder()
                .setDisplayName(gebruikersnaam)
                .build()

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                    else
                    {
                        Log.d(TAG,"User profile change denied")
                    }
                }
            listener.setUser(user,"")
        }

    }