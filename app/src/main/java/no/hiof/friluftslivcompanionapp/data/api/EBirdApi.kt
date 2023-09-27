package no.hiof.friluftslivcompanionapp.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.api.http_interface.EBirdApiService
import no.hiof.friluftslivcompanionapp.data.network.HttpClient
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.SimpleBirdSighting
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

/**
 * The `EBirdApi` class is responsible for interacting with the eBird API.
 * It uses Retrofit to make network requests and Gson to parse the JSON responses.
 *
 * @property baseUrl The base URL for the eBird API.
 * @property retrofit The Retrofit instance used to create the eBird API Service instance.
 * @property eBirdApiService The service instance used to make API calls.
 *
 * This class provides a method to get recent bird observations and map them to `Bird` objects.
 * The method `getRecentObservations` is a suspending function and should be called from a coroutine
 * or another suspending function.
 */
class EBirdApi {

    private val baseUrl = "https://api.ebird.org/"

    // Retrofit instance.
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(HttpClient.instance) // Using the singleton instance from /networks/HttpClient.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // eBird API Service instance.
    private val eBirdApiService: EBirdApiService by lazy {
        retrofit.create(EBirdApiService::class.java)
    }

    // Function to get recent bird observations. This function can only be called from
    // another 'suspend' function or from a coroutine.
    suspend fun getRecentObservations(language: String): List<Bird>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = eBirdApiService.getRecentObservations(language).execute()
                if (response.isSuccessful) {
                    response.body()?.map { mapToBird(it)}
                } else null
            }
            catch (e: Exception) {
                null
            }
        }
    }

    // Function used to map SimpleBirdSighting to a Bird object.
    private fun mapToBird(sighting: SimpleBirdSighting): Bird {
        return Bird(
            speciesNameEnglish = sighting.comName,
            speciesNameScientific = sighting.sciName,
            speciesNameNorwegian = null,
            descriptionEnglish = null,
            descriptionNorwegian = null,
            photoUrl = null,
        )
    }
}