package com.sysarcomp.clock.worldclock.api

import com.sysarcomp.clock.worldclock.model.WorldClockResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


/// Configura un timeout adecuado para evitar que las solicitudes se queden colgadas.
val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder().baseUrl("https://worldtimeapi.org/api/").client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create()).build()

val worldclockService = retrofit.create(ApiService::class.java) // le inyectamos el service que usaremos


interface ApiService {

    @GET("timezone/Europe/Madrid")
    suspend fun getWorldclockResponseMadrid(): WorldClockResponse

    @GET("timezone/America/New_York")
    suspend fun getWorldclockResponseNewyork(): WorldClockResponse

    @GET("timezone/America/Buenos_Aires")
    suspend fun getWorldclockResponseBuenosaires(): WorldClockResponse

    @GET("timezone/Asia/Tokyo")
    suspend fun getWorldclockResponseTokyo(): WorldClockResponse

    @GET("timezone/Australia/Sydney")
    suspend fun getWorldclockResponseSydney(): WorldClockResponse

}
