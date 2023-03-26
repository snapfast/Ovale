package bali.rahul.ovale.dataModel


import com.google.gson.annotations.SerializedName


data class Position(

    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null

)