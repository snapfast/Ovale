package bali.rahul.ovale.dataModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class TopicSubmissions(

    @SerializedName("current-events") var current_events: CurrentEvents? = CurrentEvents(),
    @SerializedName("wallpapers") var wallpapers: Wallpapers? = Wallpapers(),
    @SerializedName("experimental") var experimental: Experimental? = Experimental(),
    @SerializedName("arts-culture") var arts_culture: ArtsCulture? = ArtsCulture(),
    @SerializedName("spirituality") var spirituality: Spirituality? = Spirituality()

) : Parcelable {

    constructor(parcel: android.os.Parcel) : this(
        parcel.readParcelable<CurrentEvents>(CurrentEvents::class.java.classLoader)
            ?: CurrentEvents(),
        parcel.readParcelable<Wallpapers>(Wallpapers::class.java.classLoader) ?: Wallpapers(),
        parcel.readParcelable<Experimental>(Experimental::class.java.classLoader) ?: Experimental(),
        parcel.readParcelable<ArtsCulture>(ArtsCulture::class.java.classLoader) ?: ArtsCulture(),
        parcel.readParcelable<Spirituality>(Spirituality::class.java.classLoader) ?: Spirituality()
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeParcelable(current_events, flags)
        parcel.writeParcelable(wallpapers, flags)
        parcel.writeParcelable(experimental, flags)
        parcel.writeParcelable(arts_culture, flags)
        parcel.writeParcelable(spirituality, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopicSubmissions> {
        override fun createFromParcel(parcel: android.os.Parcel): TopicSubmissions {
            return TopicSubmissions(parcel)
        }

        override fun newArray(size: Int): Array<TopicSubmissions?> {
            return arrayOfNulls(size)
        }
    }


}