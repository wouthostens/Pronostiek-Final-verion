package be.vives.pronostiekappwouthostens.pronstiek.a.resultaat

import android.content.ContentValues
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PronostiekResultaatOverzichtViewModel(user: FirebaseUser, groep: Groep) : ViewModel() {
    val db = Firebase.firestore

    private val _pronstieken : MutableLiveData<ArrayList<Pronostiek>> = MutableLiveData()
    val pronstieken: LiveData<ArrayList<Pronostiek>>
        get() {
            return _pronstieken
        }
    private var _pronstiekenBeschikbaar: MutableLiveData<Boolean> = MutableLiveData()
    val pronstiekenBeschikbaar: LiveData<Boolean>
        get() {
            return _pronstiekenBeschikbaar
        }
    private var _navigatToPronostiekInvullen: MutableLiveData<Boolean> = MutableLiveData()
    val navigatToPronostiekInvullen: LiveData<Boolean>
        get() {
            return _navigatToPronostiekInvullen
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
    private var _geselecteerdePronostiek : MutableLiveData<Pronostiek> = MutableLiveData()
    val geselecteerdePronostiek : LiveData<Pronostiek>
        get() {
            return _geselecteerdePronostiek
        }
    init {
        _pronstieken.value= ArrayList()
        _pronstiekenBeschikbaar.value = false
        _groep.value = groep
        _user.value = user
        getPronostieken()
    }
    fun getPronostieken()
    {
        var test : Task<QuerySnapshot>
        db.collection("pronostieken")
            .whereEqualTo("GroepID", _groep.value!!.id)
            .get()
            .addOnCompleteListener {
                for (i in 0..it.result!!.documents.size-1) {
                    _pronstieken.value!!.add(
                        Pronostiek(
                            _groep.value!!.id,
                            it.result!!.documents.get(i).get("naam") as String,
                            null,
                            it.result!!.documents.get(i).id
                        )
                    )
                }
            }.addOnSuccessListener {
                var matchen : ArrayList<Matches>
                matchen= ArrayList()
                for (i in 0.._pronstieken.value!!.size-1)
                {
                    db.collection("pronostieken").document(_pronstieken.value!![i].id).collection("matchen").get().addOnSuccessListener { documents ->
                        matchen.clear()
                        for (document in documents) {
                            var matches = Matches(
                                document.get("matchID") as Long, document.get("statusMatch") as String
                                , document.get("leagueID") as Long,document.get("seasonId") as Long,
                                document.get("homeTeamID") as Long, document.get("homeTeamName") as String,
                                document.get("hometeamLogo") as String,document.get("awayteamID") as Long,
                                document.get("awayTeamName") as String,
                                document.get("awayTeamLogo") as String,
                                document.get("homeScore") as Long,
                                document.get("awayScore") as Long,
                                document.get("dateFrom") as String,
                                document.get("dateTo") as String,
                                document.get("datummatch") as String,true, document.get("PronostiekID") as String,"x", null)

                            matchen.add(matches)
                            Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        }
                        _pronstieken.value!![i].matchen = matchen
                        if(_pronstieken.value!![_pronstieken.value!!.size-1].matchen!=null)
                        {
                            _pronstiekenBeschikbaar.value =true
                        }
                    }
                        .addOnFailureListener { exception ->
                            Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                        }

                }
                _pronstiekenBeschikbaar.value =true
            }.addOnFailureListener {
                Log.w(ContentValues.TAG,"error : " , it)
            }
    }
    fun pronostiekenloadenFinished()
    {
        _pronstiekenBeschikbaar.value=false
    }
    fun navigeerPronostiekInvullenFinished()
    {
        _navigatToPronostiekInvullen.value = false
    }
    fun pronostiekClicked(pronostiek: Pronostiek, view: View) {
       _geselecteerdePronostiek.value = pronostiek
       _navigatToPronostiekInvullen.value = true
    }
}