package com.example.loop_new.presentation.add_flashcard

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.Flashcard
import com.example.loop_new.domain.model.api.dictionary.DictionaryResponse
import com.example.loop_new.domain.repository.InterfaceRepository
import com.example.loop_new.network.DictionaryService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFlashcardViewModel(private val interfaceRepository: InterfaceRepository) : ViewModel() {

    var meaning: String by mutableStateOf("")
    var example: String by mutableStateOf("")

    fun addFlashcard(
        word: String,
        translate: String,
//        meaning: String,
//        example: String,
//        partOfSpeech: String,
        pronunciation: String,
//        audioUrl: String,
//        isFrontVisible: Boolean,
        boxUid: String
    ) {
        val flashcardData = Flashcard(
            word = word,
            translate = translate,
//            meaning = meaning,
//            example = example,
//            partOfSpeech = partOfSpeech,
            pronunciation = pronunciation,
//            audioUrl = audioUrl,
//            isFrontVisible = isFrontVisible
        )

        viewModelScope.launch {
            interfaceRepository.addFlashcard(flashcardData, boxUid)
        }
    }

    fun fetchWord(word: String) {
        DictionaryService().dictionaryApi.getWord(word)
            .enqueue(object : Callback<List<DictionaryResponse>> {
                override fun onResponse(
                    call: Call<List<DictionaryResponse>>,
                    response: Response<List<DictionaryResponse>>
                ) {

                    if (response.isSuccessful) {
                        val dictionaryResponseList = response.body()

                        dictionaryResponseList?.let { it ->
                            if (it.isNotEmpty()) {

                                val dictionaryResponse = it[0]

                                val meanings = dictionaryResponse.meanings
                                val targetMeaning =
                                    meanings.find { it.partOfSpeech == "noun"}

                                val nonNullDefinition =
                                    targetMeaning?.definitions?.find { def -> def.definition != null }

                                // Ustaw znaczenie w ViewModel
                                meaning = nonNullDefinition?.definition ?: ""
                                example = nonNullDefinition?.example ?: ""
                            }
                        }


                    } else {
                        Log.d("Currency", "Wystąpił błąd zapytania")
//                        Toast.makeText(
//                            ,
//                            "Wystąpił błąd zapytania",
//                            Toast.LENGTH_LONG
//                        ).show()
                    }
                }


                override fun onFailure(call: Call<List<DictionaryResponse>>, t: Throwable) {

                }

            })
    }
}