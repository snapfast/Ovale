package bali.rahul.ovale.rest

import bali.rahul.ovale.dataModel.Photo
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {
    //Single refers to a single object of the type array getting from the API

    //Get a random photo from the API
    @GET("photos/random?count=30")
    fun fetchRandomPhoto(): Single<List<Photo>>

    //Get a list of photos from collection with id
    @GET("collections/{id}/photos?page=1&per_page=30")
    fun fetchCollectionPhotos(@Path("id") id: String): Single<List<Photo>>

    //Get a list of photos from a user
    @GET("users/{username}/photos?page=1&per_page=30")
    fun fetchUserPhotos(@Path("username") username: String): Single<List<Photo>>

    //Get a list of photos searched by query
    @GET("search/photos?query={query}&page=1&per_page=30")
    fun fetchSearchPhotos(@Path("query") query: String): Single<List<Photo>>

}
