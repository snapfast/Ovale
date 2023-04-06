package bali.rahul.ovale.dataModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopicSubmissions(

    @SerializedName("current-events") var current_events: CurrentEvents? = CurrentEvents(),
    @SerializedName("wallpapers") var wallpapers: Wallpapers? = Wallpapers(),
    @SerializedName("experimental") var experimental: Experimental? = Experimental(),
    @SerializedName("arts-culture") var arts_culture: ArtsCulture? = ArtsCulture(),
    @SerializedName("spirituality") var spirituality: Spirituality? = Spirituality()

) : Parcelable