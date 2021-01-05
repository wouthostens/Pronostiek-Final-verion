package be.vives.pronostiekappwouthostens.welcome

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.repo.FireStoreDBRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch


class WelcomeViewModel(__user: FirebaseUser) : ViewModel() {
    private val db = Firebase.firestore
    var fireStoreDBRepository  = FireStoreDBRepository()
    val groepenrepo =  fireStoreDBRepository._groepen
    private var groepenIds : MutableLiveData<ArrayList<String>> = MutableLiveData()
    private var _groepen : MutableLiveData<ArrayList<Groep>> = MutableLiveData()
    val groepen: LiveData<ArrayList<Groep>>
    get() {
        return _groepen
    }
    private var _groepenBeschikbaar: MutableLiveData<Boolean> = MutableLiveData()
    val groepenBeschikbaar: LiveData<Boolean>
    get() {
        return _groepenBeschikbaar
    }
    private var _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
        get(){
            return _user
        }
    private var _navigatToGroopScreen : MutableLiveData<Boolean> = MutableLiveData()
    val navigateToGroopScreen:LiveData<Boolean>
        get(){
            return _navigatToGroopScreen
        }
    private var _navigatToAansluitenGroopScreen:MutableLiveData<Boolean> = MutableLiveData()
    val navigatToAansluitenGroopScreen:LiveData<Boolean>
    get() {
        return _navigatToAansluitenGroopScreen
    }
    private var _navigatToGroepWelcomeScreen:MutableLiveData<Boolean> = MutableLiveData()
    val navigatToGroepWelcomeScreen:LiveData<Boolean>
        get() {
            return _navigatToGroepWelcomeScreen
        }
    private var _groep: MutableLiveData<Groep> = MutableLiveData()
    val groep : LiveData<Groep>
    get() {
        return _groep
    }
    val storage = Firebase.storage
    val storageRef = storage.reference

    fun navigateToGroopScreen()
    {
        _navigatToGroopScreen.value=true
    }
    fun navigateToGroopScreenFinished()
    {
        _navigatToGroopScreen.value = false
    }
    fun navigerenaansluitgroepenfinished()
    {
        _navigatToAansluitenGroopScreen.value=false
    }
    fun navigerenAansluitenGroep()
    {
        _navigatToAansluitenGroopScreen.value =true
    }
    fun navigerentogroepWelcomeFinished()
    {
        _navigatToGroepWelcomeScreen.value = false
    }
    var bmp:MutableLiveData<Bitmap> = MutableLiveData()
    init {
        _navigatToGroopScreen.value = false
        _navigatToAansluitenGroopScreen.value = false
        _navigatToGroepWelcomeScreen.value = false
         _user.value = __user

        var islandRef = storageRef.child("profilepics/" + __user.email )

        val ONE_MEGABYTE: Long = 1024 * 1024
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {

                val bytearray = it
                 bmp.value = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.size)



            // Data for "images/island.jpg" is returned, use this as needed
        }.addOnCompleteListener {
            if(bmp.value == null)
            {
                var islandRef = storageRef.child("profilepics/images.jpg" )

                val ONE_MEGABYTE: Long = 1024 * 1024
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {

                    val bytearray = it
                    bmp.value = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.size)


                    // Data for "images/island.jpg" is returned, use this as needed
                }.addOnFailureListener {
                    print(it)
                }

            }
        }
    }

    fun ophalenNaamGroepen() {

            try {
                fireStoreDBRepository.ophalenNaamGroepen(groepenIds.value!!)
                _groepen = fireStoreDBRepository._groepen
                _groepenBeschikbaar.value = true
                val a =0
            } catch (e: Exception) {
                Log.e("error: " , e.message!!)
            }


//        if (groepenIds.value!!.size > 0) {
//            db.collection("groep").get().addOnCompleteListener{
//                val size = it.result!!.documents.size
//                if (size > 0) {
//                    for (i in 0..size-1)
//                    {
//                        val groepSize = groepenIds.value!!.size
//                        for (j in 0..groepSize-1) {
//                            if (it.result!!.documents.get(i).id.equals(groepenIds.value!![j])){
//                                val groep = Groep(it.result!!.documents.get(i).id,it.result!!.documents.get(i).get("naam") as String )
//                                _groepen.value!!.add(groep)
//                            }
//                        }
//                    }
//                    _groepenBeschikbaar.value = true
//                }
//            }
//        }
    }
    fun ophalenData(){
        //ophalen groepid voor gebruiker
        groepenIds.value = ArrayList()
        _groepen.value = ArrayList()
        _groepenBeschikbaar.value = false
        db.collection("gebruikers").get().addOnCompleteListener{
            val size = it.result!!.documents.size
            if (size > 0) {
                for (i in 0..size-1)
                {
                    if(it.result!!.documents.get(i).get("id") == user.value!!.email)
                    {
                        val id = it.result!!.documents.get(i).id
                        db.collection("gebruikers").document(id).collection("groepen").get().addOnCompleteListener {
                            val size = it.result!!.documents.size
                            if (size > 0) {
                                for (i in 0..size-1) {
                                    val groepId = it.result!!.documents.get(i).get("id") as String
                                    groepenIds.value!!.add(groepId.toString())
                                }
                            }
                            ophalenNaamGroepen()
                        }
                    }
                }

            }
        }
    }

    fun groepClicked(groep: Groep) {
        _groep.value = groep
        _navigatToGroepWelcomeScreen.value = true
    }
}
