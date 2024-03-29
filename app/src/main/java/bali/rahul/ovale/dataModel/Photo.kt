package bali.rahul.ovale.dataModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    @SerializedName("exif") var exif: Exif? = Exif(),
    @SerializedName("location") var location: Location? = Location(),
    @SerializedName("meta") var meta: Meta? = Meta(),
    @SerializedName("public_domain") var publicDomain: Boolean? = null,
    @SerializedName("tags") var tags: ArrayList<Tags> = arrayListOf(),
    @SerializedName("tags_preview") var tagsPreview: ArrayList<TagsPreview> = arrayListOf(),
    @SerializedName("views") var views: Int? = null,
    @SerializedName("downloads") var downloads: Int? = null,
    @SerializedName("urls") var urls: Urls? = Urls(),
    @SerializedName("links") var links: Links? = Links(),
    @SerializedName("likes") var likes: Int? = null,
    @SerializedName("liked_by_user") var likedByUser: Boolean? = null,
    @SerializedName("current_user_collections") var currentUserCollections: ArrayList<String> = arrayListOf(),
    @SerializedName("sponsorship") var sponsorship: String? = null,
    @SerializedName("topic_submissions") var topicSubmissions: TopicSubmissions? = TopicSubmissions(),
    @SerializedName("user") var user: User? = User()


) : Parcelable