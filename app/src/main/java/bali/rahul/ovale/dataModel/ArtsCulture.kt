package bali.rahul.ovale.dataModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class ArtsCulture(

    @SerializedName("status") var status: String? = null

) : Parcelable {

    constructor(parcel: android.os.Parcel) : this(
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArtsCulture> {
        override fun createFromParcel(parcel: android.os.Parcel): ArtsCulture {
            return ArtsCulture(parcel)
        }

        override fun newArray(size: Int): Array<ArtsCulture?> {
            return arrayOfNulls(size)
        }
    }

}