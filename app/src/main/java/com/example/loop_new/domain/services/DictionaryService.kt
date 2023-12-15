package com.example.loop_new.domain.services

import com.example.loop_new.domain.model.firebase.Flashcard

interface DictionaryService {

    fun onFetchWordInfo(word: String, onFetchWordInfo : (Flashcard) -> Unit)

}