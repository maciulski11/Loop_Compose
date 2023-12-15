package com.example.loop_new.data.api

import android.util.Log
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.api.dictionary.DictionaryResponse
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.InterfaceDictionaryApi
import com.example.loop_new.domain.services.DictionaryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DictionaryService :
    DictionaryService {

    private val dictionaryRetrofit = Retrofit.Builder()
        .baseUrl("https://api.dictionaryapi.dev/api/v2/entries/en/")// Main url which will be use to every ask
        // Adds a converter to API responses to objects and convert data JSON to Kotlin
        .addConverterFactory(GsonConverterFactory.create())
        .build()// Creates the final instance of the Retrofit class

    // Calls the create method on the retrofit object to create the CurrencyApi instance
    private val interfaceDictionaryApi: InterfaceDictionaryApi =
        dictionaryRetrofit.create(InterfaceDictionaryApi::class.java)

    override fun onFetchWordInfo(word: String, onFetchWordInfo: (Flashcard) -> Unit) {
        DictionaryService().interfaceDictionaryApi.getWord(word)
            .enqueue(object : Callback<List<DictionaryResponse>> {
                override fun onResponse(
                    call: Call<List<DictionaryResponse>>,
                    response: Response<List<DictionaryResponse>>
                ) {

                    if (response.isSuccessful) {
                        val dictionaryResponseList = response.body()

                        dictionaryResponseList?.let { it ->
                            if (it.isNotEmpty()) {

                                val dictionaryResponse = it[0]

                                val dictionaryResponseModel = dictionaryResponse.meanings
                                val targetMeaning =
                                    dictionaryResponseModel.find { it.partOfSpeech == "noun" }

                                val definitionsModel =
                                    targetMeaning?.definitions?.find { def -> def.definition != null }

                                val phoneticsModel =
                                    dictionaryResponse.phonetics.find { pro -> pro.audio != null }

                                val wordInfo = Flashcard(
                                    meaning = definitionsModel?.definition ?: "",
                                    example = definitionsModel?.example ?: "",
                                    audioUrl = phoneticsModel?.audio,
                                    pronunciation = phoneticsModel?.text?.lowercase() ?: ""
                                )

                                onFetchWordInfo(wordInfo)

                                Log.d(LogTags.DICTIONARY_SERVICE, "onFetchWordInfo: Success!")
                            }
                        }
                    } else {
                        Log.e(LogTags.DICTIONARY_SERVICE, "onFetchWordInfo: Request failed with code ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<DictionaryResponse>>, t: Throwable) {
                    Log.e(LogTags.DICTIONARY_SERVICE, "onFetchWordInfo: Request failed with exception", t)
                }
            })
    }
}