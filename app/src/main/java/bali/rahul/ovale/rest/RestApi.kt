package bali.rahul.ovale.rest

import bali.rahul.ovale.auth.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RestApi {

    var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .addInterceptor(AuthInterceptor())
        .build()


    //Here we create our retrofit instance to build the networkAPI
    val retrofit: NetworkApi by lazy {
        Retrofit.Builder()
            //We provide baseurl
            .baseUrl(BASE_URL)
            //Provide gson converter to convert json to pojo class
            .addConverterFactory(GsonConverterFactory.create())
            //Provide rxjava2 call adapter to transform our response into reactive
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(client)
            //build our retrofit object
            .build()
            .create(NetworkApi::class.java)
    }

    companion object {
        //Best practise to have baseurl(endpoint) provided in static variable
        const val BASE_URL = "https://api.unsplash.com/"
    }
}
