package com.example.loop_new.domain.services

interface InterfaceApiService {
    fun onTranslationResult(word: String, onTranslateWord : (String) -> Unit)
}
