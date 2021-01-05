package be.vives.pronostiekappwouthostens.pronstiek.a.aanmaken

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.vives.pronostiekappwouthostens.api.FootballApi
import be.vives.pronostiekappwouthostens.api.FootballProperty
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.repo.FireStoreDBRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PronostiekAanmakenViewModel( user: FirebaseUser,   groep: Groep) : ViewModel() {
    val db = Firebase.firestore

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
    get() {
        return _response
    }
    private var _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
        get(){
            return _user
        }
    private var _groep: MutableLiveData<Groep> = MutableLiveData()
    val groep: LiveData<Groep>
        get(){
            return _groep
        }

    private val _footballMatches = MutableLiveData<FootballProperty>()
    val footballMatches: LiveData<FootballProperty>
    get() {
        return _footballMatches
    }
    private val _matches :MutableLiveData<ArrayList<Matches>> = MutableLiveData()
    val matches: LiveData<ArrayList<Matches>>
    get() {
        return _matches
    }
    private var _matchesBeschikbaar: MutableLiveData<Boolean> = MutableLiveData()
    val matchesBeschikbaar: LiveData<Boolean>
        get() {
            return _matchesBeschikbaar
        }
    private var _aantalSelecteerde: MutableLiveData<ArrayList<Matches>> = MutableLiveData()
    val aantalselecteerde: LiveData<ArrayList<Matches>>
    get()
    {
        return _aantalSelecteerde
    }
    private var _pronostiekklar: MutableLiveData<Boolean> = MutableLiveData()
    val pronostiekklar: LiveData<Boolean>
        get()
        {
            return _pronostiekklar
        }
    private  var _bericht: MutableLiveData<String> = MutableLiveData()
    val bericht: LiveData<String>
    get() {
        return _bericht
    }
    private var _changed: MutableLiveData<Boolean> = MutableLiveData()
    val changed: LiveData<Boolean>
        get()
        {
            return _changed
        }
     var pronsotieknaam:MutableLiveData<String> = MutableLiveData()
    init {
        _matches.value = ArrayList()
        _groep.value = groep
        _user.value = user
        _bericht.value =""
        _changed.value = false
        _aantalSelecteerde.value= ArrayList()
        _response.value=""
        pronsotieknaam.value=""
        _matchesBeschikbaar.value=false
        _pronostiekklar.value =false
        getFootballBelgium()
    }
    fun getFootballEnglish()
    {
        viewModelScope.launch {
            _footballMatches.value = FireStoreDBRepository().getFootballEnglish()
            LijstOpdelen(true)
        }
    }
    fun getFootballBelgium()
    {
        viewModelScope.launch {
            _footballMatches.value = FireStoreDBRepository().getFootballBelgium()
            LijstOpdelen(false)
            getFootballEnglish()
        }
    }
    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, +daysAgo)

        return calendar.time
    }
    fun LijstOpdelen(Klaar:Boolean)
    {
        _matchesBeschikbaar.value=false
        for (i in 0.._footballMatches.value!!.data!!.size-1)
        {
             var match =Matches(_footballMatches.value!!.data!![i].matchID,_footballMatches.value!!.data!![i].status,
                 _footballMatches.value!!.data!![i].leagueID,_footballMatches.value!!.data!![i].seasonID,
                 _footballMatches.value!!.data!![i].homeTeam.teamID, _footballMatches.value!!.data!![i].homeTeam.name,
                 _footballMatches.value!!.data!![i].homeTeam.logo,_footballMatches.value!!.data!![i].awayTeam.teamID,
                 _footballMatches.value!!.data!![i].awayTeam.name,_footballMatches.value!!.data!![i].awayTeam.logo,
                 _footballMatches.value!!.data!![i].stats.homeScore,_footballMatches.value!!.data!![i].stats.awayScore,
                 _footballMatches.value!!.query!!.dateFrom,
                 _footballMatches.value!!.query!!.dateTo,
                _footballMatches.value!!.data!![i].matchStart,false, "pronstiekIDFirebase moet hier nog komen","x", null)

            _matches.value!!.add(match)
        }
        _matches.value!!.sortBy {
             it.datummatch
        }
        if(Klaar)
        {
            _matchesBeschikbaar.value=true
        }
    }
    fun matchloadenFinished()
    {
        _matchesBeschikbaar.value =false
    }
    fun pronstiekklaarfinished()
    {
        _pronostiekklar.value = false
    }
    fun berichtfinshed()
    {
        _bericht.value=""
    }
    fun changedfinished()
    {
        _changed.value = false
    }
    fun matchClicked(matches: Matches, view:View) {
        if (_aantalSelecteerde.value!!.count() >= 10)
        {
            if(matches.isselected)
            {
                matches.isselected=false
                _changed.value=true
                _aantalSelecteerde.value!!.remove(matches)
                _matchesBeschikbaar.value = true
               // _bericht.value = "U hebt " + _aantalSelecteerde.value!!.size + "aantal matchen van de 10 geselecteerd"
            }
            else
            {
                _bericht.value="Uw pronostiek is klaar en kan worden opgeslaan"
                _pronostiekklar.value = true
            }
        } else
        {
            _pronostiekklar.value = false
            if(!matches.isselected)
            {
                matches.isselected = true
                _changed.value=true
                _aantalSelecteerde.value!!.add(matches)
                _matchesBeschikbaar.value = true
                //_bericht.value ="U hebt " + _aantalSelecteerde.value!!.size + "aantal matchen van de 10 geselecteerd"
            }
            else
            {
                matches.isselected=false
                _changed.value=true
                _aantalSelecteerde.value!!.remove(matches)
                _matchesBeschikbaar.value = true
               // _bericht.value = "U hebt " + _aantalSelecteerde.value!!.size + "aantal matchen van de 10 geselecteerd"
            }
            if(_aantalSelecteerde.value!!.count()==10)
            {
                _pronostiekklar.value = true
            }
        }
    }
    fun opslaanPronostiekFireBase()
    {
        viewModelScope.launch {
            try {
                FireStoreDBRepository().opslaanPronostiekFireBase(pronsotieknaam.value!!, groep.value!!,_aantalSelecteerde.value!!)
            } catch (e: Exception) {} }
    }
}
