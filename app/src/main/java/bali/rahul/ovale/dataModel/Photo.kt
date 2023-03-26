package bali.rahul.ovale.dataModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Photo(


    @SerializedName("id") var id: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("promoted_at") var promotedAt: String? = null,
    @SerializedName("width") var width: Int? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("color") var color: String? = null,
    @SerializedName("blur_hash") var blurHash: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("alt_description") var altDescription: String? = null,
    @SerializedName("urls") var urls: Urls? = Urls(),
    @SerializedName("links") var links: Links? = Links(),
    @SerializedName("likes") var likes: Int? = null,
    @SerializedName("liked_by_user") var likedByUser: Boolean? = null,
    @SerializedName("current_user_collections") var currentUserCollections: ArrayList<String> = arrayListOf(),
    @SerializedName("sponsorship") var sponsorship: String? = null,
    @SerializedName("topic_submissions") var topicSubmissions: TopicSubmissions? = TopicSubmissions(),
    @SerializedName("user") var user: User? = User()


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Urls::class.java.classLoader),
        parcel.readParcelable(Links::class.java.classLoader),
        parcel.readInt() ?: 0,
        parcel.readByte() != 0.toByte(),
        parcel.createStringArrayList() ?: arrayListOf(),
        parcel.readString() ?: "",
        parcel.readParcelable(TopicSubmissions::class.java.classLoader),
        parcel.readParcelable(User::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeString(promotedAt)
        parcel.writeInt(width ?: 0)
        parcel.writeInt(height ?: 0)
        parcel.writeString(color)
        parcel.writeString(blurHash)
        parcel.writeString(description)
        parcel.writeString(altDescription)
        parcel.writeParcelable(urls, flags)
        parcel.writeParcelable(links, flags)
        parcel.writeInt(likes ?: 0)
        parcel.writeByte(if (likedByUser == true) 1 else 0)
        parcel.writeStringList(currentUserCollections)
        parcel.writeString(sponsorship)
        parcel.writeParcelable(topicSubmissions, flags)
        parcel.writeParcelable(user, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }
}