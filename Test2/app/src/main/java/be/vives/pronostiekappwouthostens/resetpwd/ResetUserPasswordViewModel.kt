package be.vives.pronostiekappwouthostens.resetpwd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.vives.pronostiekappwouthostens.classes.User

class ResetUserPasswordViewModel(var __user:User) : ViewModel() {
    private var _user:MutableLiveData<User> = MutableLiveData()
    val user:LiveData<User>
    get(){
        return _user
    }
    init {
        _user.value = __user
    }
    fun resetPasswordClicked() {

    }
}