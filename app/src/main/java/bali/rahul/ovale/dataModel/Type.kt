package bali.rahul.ovale.dataModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Type(

    @SerializedName("slug") var slug: String? = null,
    @SerializedName("pretty_slug") var prettySlug: String? = null

) : Parcelable

