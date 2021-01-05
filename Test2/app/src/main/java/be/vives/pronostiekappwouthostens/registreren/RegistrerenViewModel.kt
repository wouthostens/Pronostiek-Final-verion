package be.vives.pronostiekappwouthostens.registreren

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import be.vives.pronostiekappwouthostens.api.AuthenticationHelperModel
import be.vives.pronostiekappwouthostens.classes.User
import be.vives.pronostiekappwouthostens.repo.FireStoreDBRepository
import be.vives.pronostiekappwouthostens.utils.MyAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class RegistrerenViewModel(var _myAuth: MyAuth) : AuthenticationHelperModel() {
    private  var myAuth: MyAuth

    private var _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
        get(){
            return _user
        }
    var email: MutableLiveData<String> = MutableLiveData()
    var password1: MutableLiveData<String> = MutableLiveData()
    var password2:MutableLiveData<String> = MutableLiveData()
    var gebruikersnaam: MutableLiveData<String> = MutableLiveData()
    var profilepic :MutableLiveData<Uri> = MutableLiveData()
    private var _navigateToLoginScreen: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToLoginScreen :LiveData<Boolean>
    get() {return _navigateToLoginScreen}
    private var _afbeeldingKiezen:MutableLiveData<Boolean> = MutableLiveData()
    val afbeeldKiezen: LiveData<Boolean>
    get(){return _afbeeldingKiezen}
    private var _error : MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get()
        {
            return _error
        }
    private var _cameraopenen:MutableLiveData<Boolean> = MutableLiveData()
    val cameraopenen: LiveData<Boolean>
        get(){return _cameraopenen}
    fun buttonRegistrerenClicked()
    {
        if(password1.value?.isNotEmpty()  ==true && password2.value?.isNotEmpty()==true&&email.value?.isNotEmpty()==true && gebruikersnaam.value?.isNotEmpty()==true)
        {
            if(controlerenPassword(password1.value!! , password2.value!!))
            {
                var user = User(email.value!!, password1.value!!, gebruikersnaam.value!!, profilepic.value!!)
                viewModelScope.launch {
                    try {
                        val (u, e) = FireStoreDBRepository().registreerNieuweGebruiker(user,myAuth.getContext())
                        if (u.value != null) {
                            _user.value = u.value
                        }
                        _error.value = e.value
                        Log.d("user", user.toString())
                        _navigateToLoginScreen.value = true
                    } catch (e: Exception) {} }

            }
            else
            {
                _error.value="De 2 wachtwoorden komen niet overeen"
            }
        }
        else{
            _error.value="vul alle  velden in"
        }
    }

    private fun controlerenPassword(pass1: String, pass2: String): Boolean {
        return pass1.equals(pass2)
    }
    fun AfbeeldingKiezen()
    {
        _afbeeldingKiezen.value = true
    }
    fun Afbeeldinggekozen()
    {
        _afbeeldingKiezen.value = false
    }
    fun camerageopene()
    {
        _cameraopenen.value = true
    }
    fun camereopenefinished()
    {
        _cameraopenen.value = false
    }
    fun navigateToLoginScreenFinished()
    {
        _navigateToLoginScreen.value =false
    }
    fun errorfinished()
    {
        _error.value = ""
    }
    init {
        profilepic.value
        myAuth = _myAuth
        _error.value=""
        _afbeeldingKiezen.value = false
        _cameraopenen.value=false
        _navigateToLoginScreen.value=false
    }
    //AuthenticationHelperInterface
    override fun setUser(user: FirebaseUser?, error: String) {
        if (user != null) {
            _navigateToLoginScreen.value = true
            password1.value = ""
            password2.value = ""
            email.value = ""
            gebruikersnaam.value = ""
            //updateUI(user)

        } else {
            // If sign in fails, display a message to the user.
            password1.value = ""
            password2.value = ""
            email.value = ""
            _error.value = error
        }
    }
}