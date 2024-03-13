package com.example.loop_new.domain.services

interface TranslateService {

    fun onTranslationResult(word: String, onTranslateWord: (String) -> Unit)

}
