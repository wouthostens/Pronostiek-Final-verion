package be.vives.pronostiekappwouthostens.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Pronostiek(var GroepID:String, var naam:String, var matchen: @RawValue ArrayList<Matches>??, var id :String):Parcelable {
}
