package be.vives.pronostiekappwouthostens.repo

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import be.vives.pronostiekappwouthostens.api.FootballApi
import be.vives.pronostiekappwouthostens.api.FootballProperty
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import be.vives.pronostiekappwouthostens.classes.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FireStoreDBRepository() {

    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    var _groepen : MutableLiveData<ArrayList<Groep>> = MutableLiveData()
    var _groepenBeschikbaar :MutableLiveData<Boolean> = MutableLiveData()
     fun ophalenNaamGroepen(groepenIds:ArrayList<String>) {
            _groepenBeschikbaar.value = false
            _groepen.value = ArrayList()
            if (groepenIds!!.size > 0) {
                db.collection("groep").get().addOnCompleteListener{
                    val size = it.result!!.documents.size
                    if (size > 0) {
                        for (i in 0..size-1)
                        {
                            val groepSize = groepenIds!!.size
                            for (j in 0..groepSize-1) {
                                if (it.result!!.documents.get(i).id.equals(groepenIds!![j])){
                                    val groep = Groep(it.result!!.documents.get(i).id,it.result!!.documents.get(i).get("naam") as String )
                                    _groepen.value!!.add(groep)
                                }
                            }
                        }
                        _groepenBeschikbaar.value = true
                    }
                }
            }


    }
    fun opslaanPronostiekFireBase(pronsotieknaam:String , groep: Groep , _aantalSelecteerde:List<Matches>)
    {
        var naam = pronsotieknaam
        db.collection("pronostieken").add(hashMapOf("GroepID" to groep.id , "naam" to naam)).addOnCompleteListener {
            for(i in 0.._aantalSelecteerde.size-1)
            {
                db.collection("pronostieken").document( it.result!!.id).collection("matchen").add(hashMapOf(
                    "matchID" to _aantalSelecteerde[i].matchID, "statusMatch" to _aantalSelecteerde[i].statusMatch,
                    "leagueID" to _aantalSelecteerde[i].leagueID , "seasonId" to _aantalSelecteerde[i].seasonId,
                    "homeTeamID" to _aantalSelecteerde[i].homeTeamID, "homeTeamName" to _aantalSelecteerde[i].homeTeamName,
                    "hometeamLogo" to _aantalSelecteerde[i].homeTeamLogo, "awayteamID" to _aantalSelecteerde[i].awayTeamID,
                    "awayTeamName" to _aantalSelecteerde[i].awayTeamName, "awayTeamLogo" to _aantalSelecteerde[i].awayTeamLogo,
                    "homeScore" to _aantalSelecteerde[i].homeScore, "awayScore" to _aantalSelecteerde[i].awayScore,
                    "dateFrom" to _aantalSelecteerde[i].dateFrom, "dateTo" to _aantalSelecteerde[i].dateTo,
                    "datummatch" to _aantalSelecteerde[i].datummatch, "PronostiekID" to it.result!!.id,
                    "PronostiekResultaat" to _aantalSelecteerde[i].pronostiekresultaat)
                )
            }
        }
    }
    fun pronstiekOpslaan(pronostieken:Pronostiek, groep: Groep, user: FirebaseUser) {
        var _pronstieken = pronostieken
        var _groep = groep
        var _user = user
        var pronstiekID=""
        db.collection("pronostieken").whereEqualTo("GroepID", _groep.id).get()
            .addOnSuccessListener {
                for (i in 0..it.documents.size - 1) {
                    if (it.documents[i].get("naam") == _pronstieken.naam)
                        pronstiekID = it.documents[i].id
                        db.collection("pronostieken").document(it.documents[i].id).collection("matchen").get().addOnSuccessListener {
                            for (i in 0..it.documents.size-1)
                            {
                                db.collection("pronostieken").document(pronstiekID).collection("matchen").document(it.documents[i].id).collection("pronostiekPerGebruiker").add(
                                    hashMapOf("email" to _user.email , "pronostiek" to _pronstieken.matchen!![i].pronostiekresultaat))
                            }
                        }

                }
            }
    }

     fun controlerenAansluitingsCode(aansluitingscode:String,   user: FirebaseUser )
    {
        db.collection("groep").get().addOnCompleteListener {
            val size = it.result!!.documents.size
            var codebestaat:Boolean =false
            var code: String
            code=""
            if (size>=1)
            {
                for (i in 0..size-1)
                {
                    if(it.result!!.documents.get(i).get("id").toString() == aansluitingscode)
                    {
                        codebestaat=true
                        code = it.result!!.documents.get(i).id
                    }
                }
                AansluitingscodeToevoegenAanGebruiker(codebestaat, user,code)
            }

        }
    }
    fun AansluitingscodeToevoegenAanGebruiker(CodeBetstaat:Boolean, user:FirebaseUser, groepID: String)
    {
        var gebruikerHeeftAlGroepen=false
        var gberuikersIdFirebese=""
        if (CodeBetstaat)
        {
            val tst = db.collection("gebruikers").get().addOnCompleteListener{
                val size = it.result!!.documents.size
                if (size>=1)
                {
                    for (i in 0..size-1)
                    {
                        if(it.result!!.documents.get(i).get("id") == user.email)
                        {
                            gberuikersIdFirebese = it.result!!.documents.get(i).id
                            gebruikerHeeftAlGroepen=true
                        }
                    }
                }

                if (!gebruikerHeeftAlGroepen)
                {
                    db.collection("gebruikers").add(hashMapOf("id" to user.email)).addOnCompleteListener {
                        gberuikersIdFirebese = it.result!!.id
                        db.collection("gebruikers").document(gberuikersIdFirebese).collection("groepen").add(hashMapOf("id" to groepID))
                    }
                }
                else
                {
                    db.collection("gebruikers").document(gberuikersIdFirebese).collection("groepen").add(hashMapOf("id" to groepID))
                }
            }
        }
    }


    suspend fun groepAanmaken(groepsnaam:String, user: FirebaseUser)
    {
        var code : String

        withContext(Dispatchers.IO){
            var groep: DocumentReference? = null
            code = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(java.util.Date())

            groep = db.collection("groep").add(hashMapOf("naam" to groepsnaam , "startdatum" to currentDate, "id" to code.hashCode(), "userEmail" to user.email )).await()

            gebruikerToevoegenAanGroep(groep!!.id , user)

        }

    }
    fun gebruikerToevoegenAanGroep(id: String , user: FirebaseUser) {
        var gebruikerHeeftAlGroepen=false
        var gberuikersIdFirebese=""
        val tst = db.collection("gebruikers").get().addOnCompleteListener{
            val size = it.result!!.documents.size
            if (size>=2)
            {
                for (i in 0..size-1)
                {
                    if(it.result!!.documents.get(i).get("id") == user.email)
                    {
                        gberuikersIdFirebese = it.result!!.documents.get(i).id
                        gebruikerHeeftAlGroepen=true
                    }
                }
            }
            else if(size == 1){
                if (it.result!!.documents.get(0).get("id")==user.email)
                {
                    gberuikersIdFirebese = it.result!!.documents.get(0).id
                    gebruikerHeeftAlGroepen=true
                }
            }


            if (!gebruikerHeeftAlGroepen)
            {
                db.collection("gebruikers").add(hashMapOf("id" to user.email)).addOnCompleteListener {
                    gberuikersIdFirebese = it.result!!.id
                    db.collection("gebruikers").document(gberuikersIdFirebese).collection("groepen").add(hashMapOf("id" to id))
                }
            }
            else
            {
                db.collection("gebruikers").document(gberuikersIdFirebese).collection("groepen").add(hashMapOf("id" to id))
            }
        }
    }

    suspend fun registreerNieuweGebruiker(user: User, context: Context):Pair<MutableLiveData<FirebaseUser>, MutableLiveData<String>> {
        var authUser : MutableLiveData<FirebaseUser> = MutableLiveData()
        var error: MutableLiveData<String> = MutableLiveData()

        withContext(Dispatchers.IO) {
            val task: AuthResult??

            try {
                task = auth.createUserWithEmailAndPassword(user.email, user.password).await()

                authUser.postValue(task.user!!)
                //upload profielfoto
                val storage = Firebase.storage
                val storageRef = storage.reference

                var file = user.profielfoto!!
                val riversRef = storageRef.child("profilepics/"+user.email)
                var uploadTask = riversRef.putFile(file)
                uploadTask.addOnFailureListener {
                    error.postValue("Profielfoto opladen is mislukt.")
                }.addOnSuccessListener { taskSnapshot ->
                    //error.value="foto uploaden gelukt"
                }
                var profileUpdates =   UserProfileChangeRequest.Builder()
                    .setDisplayName(user.gebruikersnaam)
                    .build()
                profileUpdates.displayName
                authUser!!.value!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(ContentValues.TAG, "User profile updated.")
                        }
                        else
                        {
                            Log.d(ContentValues.TAG,"User profile change denied")
                        }
                    }
                error.postValue("U bent succesvol geregistreerd.")
            } catch (_error:Exception){
                error.postValue("Registreren mislukt,paswoord minstens 6 karakters/zeker dat u nieuw bent?/Correcte email?")
                //error.postValue(_error.toString())
            }
        }
        return Pair(authUser, error)
    }
    suspend fun getFootballEnglish(): FootballProperty {
        var sdf = SimpleDateFormat("yyyy-M-dd")
        var currentDate = sdf.format(getDaysAgo(3))
        var eindedatum = sdf.format(getDaysAgo(10))
        var matches :FootballProperty
        matches = FootballProperty(null,null)
        withContext(Dispatchers.IO) {
            matches = FootballApi.retrofitService.getEnglishFootballmatches(currentDate, eindedatum).await()
        }
        return matches
    }
    suspend fun getFootballBelgium(): FootballProperty {
        var sdf = SimpleDateFormat("yyyy-M-dd")
        var currentDate = sdf.format(getDaysAgo(3))
        var eindedatum = sdf.format(getDaysAgo(10))
        var matches :FootballProperty
        matches = FootballProperty(null,null)
        withContext(Dispatchers.IO) {
            matches =FootballApi.retrofitService.getBelgiumFootballmatches(currentDate,eindedatum).await()
        }
        return matches
    }
    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, +daysAgo)

        return calendar.time
    }
    suspend fun ophalenPronostiek(id:String, email: String) : ArrayList<Matches>  {
        //test - "WP63sYWlognfP57BShZy"
        var matchen = ArrayList<Matches>()
        withContext(Dispatchers.IO) {
            val matchenRef = db.collection("pronostieken").document(id).collection("matchen").get().await()


            for (document in matchenRef.documents) {
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
                    document.get("datummatch") as String,true, document.get("PronostiekID") as String,"x",
                    null)

                matchen.add(matches)
                val pronostiekGebruikerRef = db.collection("pronostieken").document(id).collection("matchen").document(document.id).collection("pronostiekPerGebruiker").whereEqualTo("email", email).get().await()
                if (pronostiekGebruikerRef.size() > 0) {
                    matches.pronostiekUser = pronostiekGebruikerRef.documents[0]["pronostiek"].toString()
                }
            }
        }

    return matchen
    }

}