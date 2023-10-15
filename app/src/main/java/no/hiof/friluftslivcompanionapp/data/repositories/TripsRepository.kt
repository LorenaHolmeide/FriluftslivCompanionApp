package no.hiof.friluftslivcompanionapp.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.data.network.Result
import javax.inject.Inject

class TripsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    suspend fun saveTrip(hike: Hike) {

        val hikesCollection = firestore.collection("trips")

        val newDocumentRef = hikesCollection.document()

        val hikeWithDocumentId = hike.copy(documentId = newDocumentRef.id)

        try {

            newDocumentRef.set(hikeWithDocumentId).await()
            println(hikeWithDocumentId)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        suspend fun getHike(documentId: String): Result<Hike?> {
            return try {
                val hikesCollection = firestore.collection("trips")
                val documentRef = hikesCollection.document(documentId)

                val documentSnapshot = documentRef.get().await()

                if (documentSnapshot.exists()) {
                    val hike = documentSnapshot.toObject(Hike::class.java)
                    Result.Success(hike)
                } else {
                    Result.Failure("Hike not found")
                }
            } catch (e: Exception) {
                Result.Failure("Error: ${e.message}")
            }
        }


    }}