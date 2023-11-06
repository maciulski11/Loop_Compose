package com.example.loop_new.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DictionaryService {

    private val dictionaryRetrofit = Retrofit.Builder()
        .baseUrl("https://api.dictionaryapi.dev/api/v2/entries/en/")// Main url which will be use to every ask
        // Adds a converter to API responses to objects and convert data JSON to Kotlin
        .addConverterFactory(GsonConverterFactory.create())
        .build()// Creates the final instance of the Retrofit class

    // Calls the create method on the retrofit object to create the CurrencyApi instance
    val dictionaryApi: DictionaryApi = dictionaryRetrofit.create(DictionaryApi::class.java)
}