package com.example.loop_new.data.api

import android.util.Log
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.api.translate.TranslateRequest
import com.example.loop_new.domain.model.api.translate.TranslateResponse
import com.example.loop_new.domain.services.TranslateService
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

@DelicateCoroutinesApi
class TranslateService : TranslateService {

    override fun onTranslationResult(word: String, onTranslateWord: (String) -> Unit) {

        // Uruchomienie zapytania asynchronicznie w tle
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = translate(word)

                val responseCode = response.code
                if (responseCode == 200) {
                    val responseBody = response.body?.string()
                    val translateResponse =
                        Gson().fromJson(responseBody, TranslateResponse::class.java)
                    val translatedText = translateResponse.translations.firstOrNull()?.text

                    onTranslateWord(translatedText ?: "")

                    Log.d(LogTags.TRANSLATE_SERVICES, "onTranslationResult: Success response: $translatedText")

                } else {
                    Log.e(LogTags.TRANSLATE_SERVICES, "onTranslationResult: Error response: $responseCode")
                }

            } catch (e: Exception) {
                // Obsługa błędów
                Log.e(LogTags.TRANSLATE_SERVICES, "Translation error: $e")
                e.printStackTrace()
            }
        }
    }

    private fun translate(text: String): Response {

        val targetLang = "pl"

        val authKey = "5efb13ce-3953-00c3-74ce-d04aab640178:fx"

        val requestBody = TranslateRequest(listOf(text), targetLang)
        val requestBodyJson = Gson().toJson(requestBody)

        val mediaType = "application/json".toMediaTypeOrNull()
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api-free.deepl.com/v2/translate")
            .addHeader("Authorization", "DeepL-Auth-Key $authKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBodyJson.toRequestBody(mediaType))
            .build()

        return client.newCall(request).execute()
    }
}