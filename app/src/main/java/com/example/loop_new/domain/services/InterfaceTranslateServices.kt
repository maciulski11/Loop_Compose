package com.example.loop_new.domain.services

interface InterfaceTranslateServices {

    fun onTranslationResult(word: String, onTranslateWord : (String) -> Unit)

}
