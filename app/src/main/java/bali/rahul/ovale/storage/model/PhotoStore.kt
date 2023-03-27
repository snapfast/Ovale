package bali.rahul.ovale.storage.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(
    tableName = "ovale_photos",
    indices = [Index(value = ["date_added"])]
)
data class PhotoStore(
    @PrimaryKey var id: String = "",
    var date_added: Long? = null,
    var photo_url: String? = null,
    var photo_link: String? = null,
) : Parcelable
