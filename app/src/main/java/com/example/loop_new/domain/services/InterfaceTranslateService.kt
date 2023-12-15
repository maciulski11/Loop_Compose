package com.example.loop_new.domain.services

interface InterfaceTranslateService {

    fun onTranslationResult(word: String, onTranslateWord : (String) -> Unit)

}
