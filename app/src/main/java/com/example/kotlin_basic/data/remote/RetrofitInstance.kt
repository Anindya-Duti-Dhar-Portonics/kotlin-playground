package com.example.kotlin_basic.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//singleton
object RetrofitInstance {

    private const val BASE_URL = "https://mocki.io/v1/"

    // use by lazy : create api only when it is first used (avoids creating before it is needed)
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}