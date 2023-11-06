package com.example.loop_new.domain.model.api.translate

data class TranslateRequest(
    val text: List<String>,
    val target_lang: String
)