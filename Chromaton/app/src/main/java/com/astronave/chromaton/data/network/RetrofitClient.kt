package com.astronave.chromaton.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://chromaton-backend.onrender.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES) // Esperar 1 minuto para conectar
        .readTimeout(1, TimeUnit.MINUTES)    // Esperar 1 minuto para leer datos
        .writeTimeout(1, TimeUnit.MINUTES)   // Esperar 1 minuto para enviar datos
        .build()

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}