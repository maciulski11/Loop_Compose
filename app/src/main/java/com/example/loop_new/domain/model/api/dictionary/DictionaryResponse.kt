package com.example.loop_new.domain.model.api.dictionary

data class DictionaryResponse(
    var word: String,
    var phonetics: List<Phonetics>,
    var meanings: List<Meanings>,
    var sourceUrls: List<String>
)