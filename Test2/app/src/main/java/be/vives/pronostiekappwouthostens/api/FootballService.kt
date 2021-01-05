package be.vives.pronostiekappwouthostens.api

import com.google.type.DateTime
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat

private const val BASE_URL = "https://app.sportdataapi.com/api/v1/soccer/"

private  val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface FootballService {
    @GET("matches?apikey=c83ce520-3d30-11eb-9e2f-3fd80dbc9c1d&season_id=248")
    fun getBelgiumFootballmatches(@Query("date_from")currentDate:String, @Query("date_to")eindedate:String):
            Call<FootballProperty>
    @GET("matches?apikey=c83ce520-3d30-11eb-9e2f-3fd80dbc9c1d&season_id=352")
    fun getEnglishFootballmatches(@Query("date_from")currentDate:String, @Query("date_to")eindedate:String):
            Call<FootballProperty>

}

object FootballApi {
    val retrofitService: FootballService by lazy {
        retrofit.create(FootballService::class.java)
    }
}
