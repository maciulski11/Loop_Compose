package com.example.loop_new.presentation.screens.box

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.services.InterfaceFirebaseService
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class BoxViewModel(private val interfaceFirebaseService: InterfaceFirebaseService) : ViewModel() {

    val boxList: MutableState<List<Box>?> = mutableStateOf(null)

    private val firestore = Firebase.firestore
    private val _isListEmpty = mutableStateOf(true)
    val isListEmpty: Boolean
        get() = _isListEmpty.value

    init {
        // Wywołaj funkcję sprawdzającą stan listy w Firestore w konstruktorze lub odpowiednim miejscu
        checkFirestoreCollection()
        setupFirestoreListener()
        fetchListOfBox()
    }

    private fun setupFirestoreListener() {
        firestore.collection("repeat")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    // Obsłuż błąd
                    Log.e("YourViewModel", "Firestore listener error: $error")
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    // Aktualizuj stan na podstawie aktualnych danych z Firestore
                    _isListEmpty.value = querySnapshot.isEmpty
                }
            }
    }

    private fun checkFirestoreCollection() {
        firestore.collection("repeat")
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Sprawdź czy kolekcja jest pusta i ustaw odpowiedni stan
                _isListEmpty.value = querySnapshot.isEmpty
            }
            .addOnFailureListener { exception ->
                // Obsłuż ewentualny błąd
                Log.e("YourViewModel", "checkFirestoreCollection: $exception")
            }
    }

    private fun fetchListOfBox() {
        viewModelScope.launch {
            try {
                val boxFlow = interfaceFirebaseService.fetchListOfBox()
                boxFlow.collect { boxes ->
                    boxList.value = boxes

                    Log.d(LogTags.MAIN_VIEW_MODEL, "fetchListOfBox: Success!")
                }
            } catch (e: Exception) {

                Log.e(LogTags.MAIN_VIEW_MODEL, "fetchListOfBox: Error: $e")
            }
        }
    }

    fun addBox(name: String, describe: String, colorGroup: List<Color>) {

        // Przekształć kolory do formatu HEX
        val color1 = colorToHex(colorGroup[0])
        val color2 = colorToHex(colorGroup[1])
        val color3 = colorToHex(colorGroup[2])

        val box = Box(name = name, describe = describe, color1 = color1, color2 = color2, color3 = color3)
        viewModelScope.launch {
            try {
                interfaceFirebaseService.addBox(box)

                Log.d(LogTags.MAIN_VIEW_MODEL, "addBox: Correct addition of box")

            } catch (e: Exception) {

                Log.e(LogTags.MAIN_VIEW_MODEL, "addBox: Error: $e")
            }
        }
    }

    // Helper function to convert color to HEX format
    private fun colorToHex(color: Color): String {
        return "#%02x%02x%02x".format(
            (color.red * 255).toInt(),
            (color.green * 255).toInt(),
            (color.blue * 255).toInt()
        )
    }
}



