package bali.rahul.ovale.dataModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Spirituality(

    @SerializedName("status") var status: String? = null,
    @SerializedName("approved_on") var approvedOn: String? = null

) : Parcelable {

    constructor(parcel: android.os.Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeString(approvedOn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Spirituality> {
        override fun createFromParcel(parcel: android.os.Parcel): Spirituality {
            return Spirituality(parcel)
        }

        override fun newArray(size: Int): Array<Spirituality?> {
            return arrayOfNulls(size)
        }
    }


}