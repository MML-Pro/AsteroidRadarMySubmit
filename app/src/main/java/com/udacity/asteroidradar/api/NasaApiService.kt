package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class NasaApiService {

    interface AsteroidService {
        @GET("neo/rest/v1/feed")
        fun getAsteroids(
            @Query("api_key") api_key: String
        ): Call<String>
//
//        @GET("planetary/apod")
//        suspend fun getPictureOfTheDay(
//            @Query("api_key") api_key: String
//        ): PictureOfDay
    }

    object AsteroidApi {
//        private val moshi = Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
//            .build()

        private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val retrofitService: AsteroidService by lazy { retrofit.create(AsteroidService::class.java) }
    }
}