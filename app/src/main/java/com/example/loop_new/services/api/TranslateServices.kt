package com.example.loop_new.services.api

import com.example.loop_new.domain.model.api.translate.TranslateRequest
import com.example.loop_new.domain.model.api.translate.TranslateResponse
import com.example.loop_new.domain.services.InterfaceApiService
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

@DelicateCoroutinesApi
class TranslateService : InterfaceApiService {

    override fun onTranslationResult(word: String, onTranslateWord: (String) -> Unit) {

        val targetLang = "pl"

        // Uruchomienie zapytania asynchronicznie w tle
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = translate(word, targetLang)

                val responseCode = response.code
                if (responseCode == 200) {
                    val responseBody = response.body?.string()
                    val translateResponse =
                        Gson().fromJson(responseBody, TranslateResponse::class.java)
                    val translatedText = translateResponse.translations.firstOrNull()?.text
                    println("Translated text: $translatedText")

                    onTranslateWord(translatedText ?: "")

                } else {
                    println("Error response: $responseCode")
                }
            } catch (e: Exception) {
                // Obsługa błędów
                e.printStackTrace()
            }
        }
    }

    fun translate(text: String, targetLang: String): Response {

        val authKey = "5efb13ce-3953-00c3-74ce-d04aab640178:fx"

        val requestBody = TranslateRequest(listOf(text), targetLang)
        val requestBodyJson = Gson().toJson(requestBody)

        val mediaType = "application/json".toMediaTypeOrNull()
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api-free.deepl.com/v2/translate")
            .addHeader("Authorization", "DeepL-Auth-Key $authKey")
            .addHeader("Content-Type", "application/json")
            .post(RequestBody.create(mediaType, requestBodyJson))
            .build()

        return client.newCall(request).execute()
    }
}