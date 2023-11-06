package com.example.loop_new.network

import com.example.loop_new.domain.model.api.dictionary.DictionaryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    // Method HTTP -> @GET(variable in path to download api for app)
    @GET("{word}")
    // @Path("base") -> It'll be replaced with the real api string, witch you need
    fun getWord(@Path("word") word: String): Call<List<DictionaryResponse>>// Callback method for api question
}