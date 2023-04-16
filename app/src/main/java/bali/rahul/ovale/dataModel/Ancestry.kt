package bali.rahul.ovale.dataModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ancestry(

    @SerializedName("type") var type: Type? = Type(),
    @SerializedName("category") var category: Category? = Category(),
    @SerializedName("subcategory") var subcategory: Subcategory? = Subcategory()

) : Parcelable
