package com.example.loop_new.domain.model.api.dictionary

data class Meanings(
    var partOfSpeech: String? = null,
    var definitions: List<Definitions>? = null,
    var synonyms: List<String>? = null,
    var antonyms: List<String>? = null
)