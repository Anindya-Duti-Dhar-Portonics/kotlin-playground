package com.example.kotlin_basic.data.remote

import com.example.kotlin_basic.data.model.User
import retrofit2.http.GET

//https://mocki.io/v1/60ffc2c3-7a70-4443-9f19-3ac773eaa4df
interface ApiService {
    @GET("60ffc2c3-7a70-4443-9f19-3ac773eaa4df")
    //suspend will not block thread. it will pause coroutine and keep main thread free
    // so that other work can perform and later it resume after finish suspend
    suspend fun getUsers(): List<User>
}