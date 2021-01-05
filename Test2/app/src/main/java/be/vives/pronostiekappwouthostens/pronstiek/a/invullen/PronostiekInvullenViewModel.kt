package be.vives.pronostiekappwouthostens.pronstiek.a.invullen

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import be.vives.pronostiekappwouthostens.repo.FireStoreDBRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class PronostiekInvullenViewModel(user: FirebaseUser, pronostiek: Pronostiek, groep: Groep) : ViewModel() {
    val db = Firebase.firestore

    private val _pronstieken: MutableLiveData<Pronostiek> = MutableLiveData()
    val pronstieken: LiveData<Pronostiek>
        get() {
            return _pronstieken
        }
    private val _groep: MutableLiveData<Groep> = MutableLiveData()
    val groep: LiveData<Groep>
        get() {
            return _groep
        }
    private val _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
        get() {
            return _user
        }
    private var _pronstiekenBeschikbaar: MutableLiveData<Boolean> = MutableLiveData()
    val pronstiekenBeschikbaar: LiveData<Boolean>
        get() {
            return _pronstiekenBeschikbaar
        }
    private var _matchenbeschikbaar: MutableLiveData<Boolean> = MutableLiveData()
    val matchenbeschikbaar: LiveData<Boolean>
        get() {
            return _matchenbeschikbaar
        }
    private var _changed: MutableLiveData<Boolean> = MutableLiveData()
    val changed: LiveData<Boolean>
        get()
        {
            return _changed
        }
    private val _matchesList : MutableLiveData<ArrayList<Matches>> = MutableLiveData()
    val matchesList: LiveData<ArrayList<Matches>>
        get() {
            return _matchesList
        }
    private var _matchesAvailable: MutableLiveData<Boolean> = MutableLiveData()
    val matchesAvailable : LiveData<Boolean>
        get() {
            return _matchesAvailable
        }
    init {
        _pronstieken.value = pronostiek
        _groep.value = groep
        _user.value = user
        _pronstiekenBeschikbaar.value = true
        _matchenbeschikbaar.value = false
        _changed.value = false
        _matchesList.value=ArrayList()
        viewModelScope.launch {
            _matchesList.value = FireStoreDBRepository().ophalenPronostiek(pronostiek.id,user.email!!)
            Log.d("debug", "opgehaalde matchen " + _matchesList.value!!.count())
        }
    }
    fun  matchenbischikbaar()
    {
        _matchenbeschikbaar.value = true
    }
    fun  matchenbischikbaarfinisheed()
    {
        _matchenbeschikbaar.value = false
    }
    fun matchegeladenFinished(){
        _pronstiekenBeschikbaar.value=false
    }
    fun changedFinished(){
        _changed.value = false
    }
    fun pronstiekOpslaan()
    {
        viewModelScope.launch {
             FireStoreDBRepository().pronstiekOpslaan(_pronstieken.value!! ,_groep.value!! , user.value!!)
        }
    }
    fun pronostiekClicked(matches:Matches, view: View) {
        _changed.value = true
        if(matches.pronostiekresultaat =="x")
        {
            matches.pronostiekresultaat="2"
        }else if(matches.pronostiekresultaat=="2")
        {
            matches.pronostiekresultaat="1"
        }
        else{
            matches.pronostiekresultaat="x"
        }
    }}