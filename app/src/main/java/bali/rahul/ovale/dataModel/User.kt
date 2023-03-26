package bali.rahul.ovale.dataModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class User(

    @SerializedName("id") var id: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("twitter_username") var twitterUsername: String? = null,
    @SerializedName("portfolio_url") var portfolioUrl: String? = null,
    @SerializedName("bio") var bio: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("links") var links: Links? = Links(),
    @SerializedName("profile_image") var profileImage: ProfileImage? = ProfileImage(),
    @SerializedName("instagram_username") var instagramUsername: String? = null,
    @SerializedName("total_collections") var totalCollections: Int? = null,
    @SerializedName("total_likes") var totalLikes: Int? = null,
    @SerializedName("total_photos") var totalPhotos: Int? = null,
    @SerializedName("accepted_tos") var acceptedTos: Boolean? = null,
    @SerializedName("for_hire") var forHire: Boolean? = null,
    @SerializedName("social") var social: Social? = Social()

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable<Links>(Links::class.java.classLoader),
        parcel.readParcelable(ProfileImage::class.java.classLoader),
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readInt() ?: 0,
        parcel.readInt() ?: 0,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readParcelable(Social::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(updatedAt)
        parcel.writeString(username)
        parcel.writeString(name)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(twitterUsername)
        parcel.writeString(portfolioUrl)
        parcel.writeString(bio)
        parcel.writeString(location)
        parcel.writeParcelable(links, flags)
        parcel.writeParcelable(profileImage, flags)
        parcel.writeString(instagramUsername)
        parcel.writeInt(totalCollections ?: 0)
        parcel.writeInt(totalLikes ?: 0)
        parcel.writeInt(totalPhotos ?: 0)
        parcel.writeByte(if (acceptedTos == true) 1 else 0)
        parcel.writeByte(if (forHire == true) 1 else 0)
        parcel.writeParcelable(social, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
