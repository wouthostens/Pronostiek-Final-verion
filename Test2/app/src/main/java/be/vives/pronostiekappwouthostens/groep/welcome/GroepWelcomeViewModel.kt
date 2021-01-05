package be.vives.pronostiekappwouthostens.groep.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.vives.pronostiekappwouthostens.classes.Groep
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GroepWelcomeViewModel(user: FirebaseUser, groep: Groep) : ViewModel() {
   val db = Firebase.firestore
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
    private var _groep: MutableLiveData<Groep> = MutableLiveData()
    val groep:LiveData<Groep>
    get() {
        return _groep
    }
    private var _navigateToPronostiekAanmaken : MutableLiveData<Boolean> = MutableLiveData()
    val  navigateToPronostiekAanmaken:LiveData<Boolean>
    get() {
        return _navigateToPronostiekAanmaken
    }
    private var _navigeerPronostiekenResultaten : MutableLiveData<Boolean> = MutableLiveData()
    val   navigeerPronostiekenResultaten:LiveData<Boolean>
        get() {
            return _navigeerPronostiekenResultaten
        }
    private var _navigeerPronostiekenOverzicht : MutableLiveData<Boolean> = MutableLiveData()
    val   navigeerPronostiekenOverzicht:LiveData<Boolean>
        get() {
            return _navigeerPronostiekenOverzicht
        }
    fun navigeerPronostiekAanmaken()
    {
        _navigateToPronostiekAanmaken.value=true
    }
    fun navigeerPronostiekenOverzicht()
    {
        _navigeerPronostiekenOverzicht.value = true
    }
    fun navigeerPronostiekenResultaten()
    {
        _navigeerPronostiekenResultaten.value = true
    }
    fun navigeerPronostiekResultatenFinished()
    {
        _navigeerPronostiekenResultaten.value = false
    }
    fun navigeerPronostiekAanmakenFinished()
    {
        _navigateToPronostiekAanmaken.value=false
    }
    fun navigeerPronostiekenOverzichtFinished()
    {
        _navigeerPronostiekenOverzicht.value = false
    }
    init {
        _user.value = user
        _groep.value = groep
        _error.value = ""
        _navigateToPronostiekAanmaken.value=false
    }

    fun errorfinished()
    {
        _error.value = ""
    }


}