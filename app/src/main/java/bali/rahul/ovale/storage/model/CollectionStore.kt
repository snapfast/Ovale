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
    tableName = "ovale_collections",
    indices = [Index(value = ["date_added"])]
)
data class CollectionStore(

    @PrimaryKey val id: String = "",
    val title: String? = null,
    val user_name: String? = null,
    val cover_photo: String? = null,
    var date_added: Long? = null
) : Parcelable
