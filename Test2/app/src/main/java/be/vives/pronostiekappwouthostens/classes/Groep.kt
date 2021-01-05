package be.vives.pronostiekappwouthostens.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Groep(var id:String, var naam: String):Parcelable {
}