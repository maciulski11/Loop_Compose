package com.example.loop_new.domain.model.api.dictionary

data class Definitions(
    var definition: String? = null,
    var synonyms: List<String>? = null,
    var antonyms: List<String>? = null,
    var example: String? = null,
)