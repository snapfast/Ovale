package bali.rahul.ovale.dataModel

import com.google.gson.annotations.SerializedName


data class Meta(

    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("index") var index: Boolean? = null

)