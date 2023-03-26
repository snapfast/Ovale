package bali.rahul.ovale.dataModel

import com.google.gson.annotations.SerializedName


data class Tags(

    @SerializedName("type") var type: String? = null,
    @SerializedName("title") var title: String? = null

)