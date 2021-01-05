package be.vives.pronostiekappwouthostens.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.vives.pronostiekappwouthostens.utils.MyAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginViewModel(var _myAuth: MyAuth) : ViewModel() {
    private var myAuth: MyAuth
    private var _user:MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
    get(){
        return _user
    }
     var email:MutableLiveData<String> = MutableLiveData()
    var password:MutableLiveData<String> = MutableLiveData()

    private var _navigatToHomeScreen : MutableLiveData<Boolean> = MutableLiveData()
    val navigateToHomeScree:LiveData<Boolean>
        get(){
            return _navigatToHomeScreen
        }
    private var _navigateToResetPassword : MutableLiveData<Boolean> = MutableLiveData()
    val naviagteToResetPassword : LiveData<Boolean>
    get()
    {
        return _navigateToResetPassword
    }
    private var _error : MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
    get()
    {
        return _error
    }
    private var _navigateToRegistreren: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToRegistreren:LiveData<Boolean>
    get(){
        return _navigateToRegistreren
    }
    private var _emailsend: MutableLiveData<Boolean> = MutableLiveData()
    val emailsend:LiveData<Boolean>
    get() {return _emailsend}
    fun buttonResetClicked()
    {
        if(email.value?.isNotEmpty()==true)
        {
            val emailAddress = email.value!!
            myAuth.getAuth().sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        _emailsend.value=true
                    }
                    else
                    {
                        _error.value="vul een geldig email adres in"
                    }
                }
        }
        else{
            _error.value="Vul een email adres in"
        }
    }
    fun buttonRegistrerenClicked()
    {
        _navigateToRegistreren.value = true
    }
    fun buttonLoginClicked()
    {
        if(email.value?.isNotEmpty() == true && password.value?.isNotEmpty() ==true)
        {
            myAuth.getAuth().signInWithEmailAndPassword(email.value!!, password.value!!)
                .addOnCompleteListener(myAuth.getContext()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        _user.value =  myAuth.getAuth().currentUser
                        _navigatToHomeScreen.value = true
                        password.value=""
                        email.value=""
                        //updateUI(user)
                    } else {
                        password.value=""
                        email.value=""
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        _error.value = "Combinatie email & paswoord is niet correct"
                    }

                    // ...
                }
        }
        else
        {
            _error.value="Vul email en wachtwoord in"
        }



    }
//    fun Control(emailValue: String , passwordValue: String): Boolean {
//       // return _user.value!!.email == emailValue && _user.value!!.password == passwordValue
//    }
    fun navigateToResetPasswordFinisshed()
    {
        _navigateToResetPassword.value = false
    }
    fun navigateToHomeScreenFinisshed()
    {
        _navigatToHomeScreen.value = false
    }
    fun navigateToRegistrerenFinished()
    {
        _navigateToRegistreren.value = false
    }
    fun errorfinished()
    {
        _error.value = ""
        email.value=""
        password.value=""
    }
    fun emailsendfinished()
    {
        _emailsend.value=false
    }
    init {
        myAuth = _myAuth
        //_user.value = MockupUserDB().getUser()
        _navigateToRegistreren.value=false
        _error.value = ""
        _navigatToHomeScreen.value= false
        _navigateToResetPassword.value = false
        _emailsend.value=false

    }





}