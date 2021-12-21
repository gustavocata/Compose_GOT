package com.example.compose_got.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface SeriesService{
    @GET("?t=Game OF Thrones&Season=1&apikey=")
    suspend fun getGotSeasonOne(): Series

    @GET(value = "/")
    suspend fun getPoster(
        @Query( "i")
        imdbId: String,
        @Query("apikey")
        apiKey: String = ""
    ) : Poster
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://www.omdbapi.com/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

val seriesService: SeriesService = retrofit.create(SeriesService::class.java)