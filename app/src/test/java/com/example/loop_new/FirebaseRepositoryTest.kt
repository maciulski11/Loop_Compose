package com.example.loop_new

import com.example.loop_new.repository.FirebaseRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class FirebaseRepositoryTest() {

    // Inicjalizacja atrap obiektów do testów
    private val firebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
    private val collectionReference = Mockito.mock(CollectionReference::class.java)
    private val documentReference = Mockito.mock(DocumentReference::class.java)

    // Inicjalizacja repozytorium, które będziemy testować
    private val repository = FirebaseRepository(firebaseFirestore)

    // Metoda inicjalizacyjna przed testem
    @Before
    fun setup() {
        // Mockowanie zachowania obiektów Firestore
        Mockito.`when`(firebaseFirestore.collection(FirebaseRepository.BOX))
            .thenReturn(collectionReference)
        Mockito.`when`(collectionReference.document(Mockito.anyString()))
            .thenReturn(documentReference)
    }

    // Test jednostkowy metody addBox z repozytorium
    @Test
    suspend fun testAddBox() {
        val name = "Test Box"
        val describe = "Test description"

        // Wywołanie metody, którą testujemy
        repository.addBox(name, describe)

        // Weryfikacja, czy odpowiednie metody Firestore zostały wywołane
        verify(documentReference).set(Mockito.any())
    }


//    @Test
//    fun testFetchAllBoxes() = runBlockingTest {
//        // Przykładowe dane do testu
//        val sampleData = listOf(
//            Box("Box 1", "Description 1", "1"),
//            Box("Box 2", "Description 2", "2")
//        )
//
//        // Symulowanie zachowania Firestore
//        val querySnapshot = Mockito.mock(QuerySnapshot::class.java)
//        Mockito.`when`(querySnapshot.documents).thenReturn(sampleData.map { box ->
//            val documentSnapshot = Mockito.mock(DocumentSnapshot::class.java)
//            Mockito.`when`(documentSnapshot.toObject(Box::class.java)).thenReturn(box)
//            documentSnapshot
//        })
//
//        Mockito.`when`(collectionReference.addSnapshotListener(Mockito.any()))
//            .thenAnswer { invocation ->
//                val listener = invocation.getArgument(0) as EventListener<QuerySnapshot>
//                listener.onEvent(querySnapshot, null)
//            }
//
//        // Testowanie metody fetchAllBoxes
//        val resultFlow: Flow<List<Box>> = repository.fetchAllBoxes()
//
//        // Zebranie wyników
//        val result = mutableListOf<List<Box>>()
//        resultFlow.collect { boxes ->
//            result.add(boxes)
//        }
//
//        // Weryfikacja
//        verify(collectionReference).addSnapshotListener(Mockito.any())
//
//        // Sprawdzenie wyniku
//        assert(result == listOf(sampleData))
//    }
}



