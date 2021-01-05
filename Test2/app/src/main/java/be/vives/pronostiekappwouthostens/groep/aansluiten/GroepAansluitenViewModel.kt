package be.vives.pronostiekappwouthostens.groep.aansluiten

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.vives.pronostiekappwouthostens.repo.FireStoreDBRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class GroepAansluitenViewModel(user: FirebaseUser) : ViewModel() {
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
    private var _navigatToHomeScreen : MutableLiveData<Boolean> = MutableLiveData()
    val navigateToHomeScree:LiveData<Boolean>
        get(){
            return _navigatToHomeScreen
        }

    var aansluitingscode: MutableLiveData<String> = MutableLiveData()
    var groepId : MutableLiveData<String> = MutableLiveData()
    fun controlerenAansluitingsCode()
    {
        viewModelScope.launch {
            try {
                FireStoreDBRepository().controlerenAansluitingsCode(aansluitingscode.value.toString(), user.value!!)
                _navigatToHomeScreen.value=true;
            }
            catch (e: Exception) {
                    _error.value="De code is niet correct"
            }
        }
    }
    fun errorfinished()
    {
        _error.value = ""
    }
    fun navigateToHomeScreenFinished()
    {
        _navigatToHomeScreen.value = false
    }
    init {
        _user.value = user
        _navigatToHomeScreen.value=false
        _error.value = ""
    }

}