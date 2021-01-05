package be.vives.pronostiekappwouthostens.groep.aan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.vives.pronostiekappwouthostens.repo.FireStoreDBRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.core.FirestoreClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.w3c.dom.Document
import java.text.SimpleDateFormat
import kotlin.random.Random

class GroepAanViewModel(user: FirebaseUser) : ViewModel() {
    val db = Firebase.firestore
    var groepsnaam: MutableLiveData<String> = MutableLiveData()
    private var _error : MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get()
        {
            return _error
        }
    private var _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
        get(){
            return _user
        }
    private var _navigatToHomeScreen : MutableLiveData<Boolean> = MutableLiveData()
    val navigateToHomeScree:LiveData<Boolean>
        get(){
            return _navigatToHomeScreen
        }
    var code : MutableLiveData<String> = MutableLiveData()
    private var groep: Task<DocumentReference>? = null
    fun groepaanmaken()
    {
        if(groepsnaam.value != null)
        {
            viewModelScope.launch {
                try {
                     FireStoreDBRepository().groepAanmaken(groepsnaam.value.toString(), user.value!!)

                    Log.d("user", user.toString())
                    _navigatToHomeScreen.value = true
                } catch (e: Exception) {} }

        }
        else
        {
            _error.value="Groep aanmaken mislukt, Vul een naam in"
        }
    }
    fun navigateToHomeScreenFinished()
    {
        _navigatToHomeScreen.value = false
    }
    fun errorfinished()
    {
        _error.value = ""
    }

    init {
        _user.value = user
        _navigatToHomeScreen.value = false
        _error.value = ""
    }
}