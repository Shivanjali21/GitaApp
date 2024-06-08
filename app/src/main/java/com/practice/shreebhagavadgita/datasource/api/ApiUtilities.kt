package com.practice.shreebhagavadgita.datasource.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiUtilities {

    private val headers = mapOf(
        "Accept" to "application/json",
        "X-RapidAPI-Key" to "API Key",
        "X-RapidAPI-Host" to "Host Name"
    )

    private val client = OkHttpClient.Builder().apply {
       addInterceptor { chain ->
          val newRequest = chain.request().newBuilder().apply {
             headers.forEach { (key, value) -> addHeader(key, value) }
          }.build()
          chain.proceed(newRequest)
       }
    }.connectTimeout(5, TimeUnit.MINUTES).build()

    val api : ApiInterface by lazy {
        Retrofit.Builder()
           .baseUrl("https://bhagavad-gita3.p.rapidapi.com/")
           .addConverterFactory(GsonConverterFactory.create())
           .client(client)
           .build()
           .create(ApiInterface::class.java)
    }
}