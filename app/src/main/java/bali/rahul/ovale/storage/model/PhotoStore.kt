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
    tableName = "ovale_photos", indices = [Index(value = ["date_added"])]
)
data class PhotoStore(
    @PrimaryKey var id: String,
    var date_added: Long? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var promotedAt: String? = null,
    var width: Int? = null,
    var height: Int? = null,
    var color: String? = null,
    var blurHash: String? = null,
    var description: String? = null,
    var altDescription: String? = null,
    var url_regular: String? = null,
    var url_full: String? = null,
    var link: String? = null,
    var user: String? = null,
    var username: String? = null,
    var likes: Int? = null,


    ) : Parcelable
