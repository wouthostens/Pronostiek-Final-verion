package be.vives.pronostiekappwouthostens.pronstiek.a.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.vives.pronostiekappwouthostens.api.FootballApi
import be.vives.pronostiekappwouthostens.api.FootballProperty
import be.vives.pronostiekappwouthostens.classes.Groep
import be.vives.pronostiekappwouthostens.classes.Matches
import be.vives.pronostiekappwouthostens.classes.Pronostiek
import be.vives.pronostiekappwouthostens.repo.FireStoreDBRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PronostiekDetailViewModel(user: FirebaseUser, pronostiek: Pronostiek, groep: Groep) : ViewModel() {

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
    private var _score: MutableLiveData<Int> = MutableLiveData()
    val score : LiveData<Int>
        get() {
            return _score
        }
    init {
        _score.value = 0
        viewModelScope.launch {
            _matchesList.value = FireStoreDBRepository().ophalenPronostiek(pronostiek.id, user.email!!)
            Log.d("debug", "opgehaalde matchen " + _matchesList.value!!.count())
            ophalenMatchesViaApi()
        }

    }

    fun ophalenMatchesViaApi(){
        //ophalen resultaten matches
        if (_matchesList.value!!.count() > 0 ) {
            FootballApi.retrofitService.getBelgiumFootballmatches(_matchesList.value!![0].dateFrom,_matchesList.value!![0].dateTo).enqueue(object:
                Callback<FootballProperty> {
                override fun onResponse(call: Call<FootballProperty>, response: Response<FootballProperty>) {
                    val footballMatches = ArrayList<Matches>()
                    for (i in 0..response.body()!!.data!!.size-1)
                    {
                        var result = "x"
                        if (response.body()!!.data!![i].stats.homeScore > response.body()!!.data!![i].stats.awayScore) {
                            result = "1"
                        }
                        if (response.body()!!.data!![i].stats.homeScore < response.body()!!.data!![i].stats.awayScore) {
                            result = "2"
                        }

                        try {
                            var match = _matchesList.value!!.first {
                                it.matchID == response.body()!!.data!![i].matchID
                            }
                            var index = _matchesList.value!!.indexOf(match)
                            if (index > 0) {
                                matchesList.value!![index].pronostiekresultaat = result
                                matchesList.value!![index].homeScore = response.body()!!.data!![i].stats.homeScore
                                matchesList.value!![index].awayScore = response.body()!!.data!![i].stats.awayScore
                                //ToDO - Save match in Firestore
                            }


                        } catch (e:Exception){
                            Log.d("debug", e.toString())
                        }

                    }
                    _score.value =0
                    for (i in 0..matchesList.value!!.size-1)
                    {
                        if(matchesList.value!![i].pronostiekresultaat == matchesList.value!![i].pronostiekUser)
                        {
                            _score.value =_score.value!!.plus(1)
                        }
                    }
                    _matchesAvailable.value = true

                }

                override fun onFailure(call: Call<FootballProperty>, t: Throwable) {
                    _matchesAvailable.value = true

                }
            })
            FootballApi.retrofitService.getEnglishFootballmatches(_matchesList.value!![0].dateFrom,_matchesList.value!![0].dateTo).enqueue(object:
                Callback<FootballProperty> {
                override fun onResponse(call: Call<FootballProperty>, response: Response<FootballProperty>) {
                    val footballMatches = ArrayList<Matches>()
                    for (i in 0..response.body()!!.data!!.size-1)
                    {
                        var result = "x"
                        if (response.body()!!.data!![i].stats.homeScore > response.body()!!.data!![i].stats.awayScore) {
                            result = "1"
                        }
                        if (response.body()!!.data!![i].stats.homeScore < response.body()!!.data!![i].stats.awayScore) {
                            result = "2"
                        }

                        try {
                            var match = _matchesList.value!!.first {
                                it.matchID == response.body()!!.data!![i].matchID
                            }
                            var index = _matchesList.value!!.indexOf(match)
                            if (index > 0) {
                                matchesList.value!![index].pronostiekresultaat = result
                                matchesList.value!![index].homeScore = response.body()!!.data!![i].stats.homeScore
                                matchesList.value!![index].awayScore = response.body()!!.data!![i].stats.awayScore
                                //ToDO - Save match in Firestore
                            }


                        } catch (e:Exception){
                            Log.d("debug", e.toString())
                        }

                    }
                    _score.value =0
                    for (i in 0..matchesList.value!!.size-1)
                    {
                        if(matchesList.value!![i].pronostiekresultaat == matchesList.value!![i].pronostiekUser)
                        {
                            _score.value =_score.value!!.plus(1)
                        }
                    }
                    _matchesAvailable.value = true

                }

                override fun onFailure(call: Call<FootballProperty>, t: Throwable) {
                    _matchesAvailable.value = true

                }
            })
        }

    }

    fun listRefreshed() {
        _matchesAvailable.value = false
    }
}