package be.vives.pronostiekappwouthostens.classes

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(var email:String , var password : String, var gebruikersnaam: String, var profielfoto:Uri):Parcelable  {
}