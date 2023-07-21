package bali.rahul.ovale.dataModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(

    @SerializedName("ancestry") var ancestry: Ancestry? = Ancestry(),
    @SerializedName("title") var title: String? = null,
    @SerializedName("subtitle") var subtitle: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("meta_title") var metaTitle: String? = null,
    @SerializedName("meta_description") var metaDescription: String? = null,
    @SerializedName("cover_photo") var coverPhoto: CoverPhoto? = CoverPhoto()

) : Parcelable
