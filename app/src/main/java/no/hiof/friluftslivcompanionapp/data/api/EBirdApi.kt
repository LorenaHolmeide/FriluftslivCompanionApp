package no.hiof.friluftslivcompanionapp.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.api.http_interface.EBirdApiService
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.data.network.RetrofitBuilder
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.api.SimpleBirdSighting
import no.hiof.friluftslivcompanionapp.models.api.SimpleWikipediaResponse
import java.lang.Exception

/**
 * The `EBirdApi` class is responsible for interacting with the eBird API and Wikipedia API.
 * It uses Retrofit to make network requests and Gson to parse the JSON responses.
 *
 * @property eBirdApiService The service instance used to make API calls to eBird API.
 *
 * This class provides a method to get recent bird observations, enrich them with additional
 * information from Wikipedia, and map them to `Bird` objects.
 * The method `getRecentObservations` is a suspending function and should be called from a coroutine
 * or another suspending function.
 *
 * The `getBirdInformation` method is used to fetch additional bird information from Wikipedia API
 * based on the scientific name of the bird from the eBird API response.
 *
 * The `mapToBird` method is used to map `SimpleBirdSighting` objects to `Bird` objects, enriching
 * them with additional information fetched from Wikipedia.
 */
class EBirdApi {

    // eBird API Service instance.
    private val eBirdApiService: EBirdApiService by lazy {
        val retrofit = RetrofitBuilder.buildEBirdApi()
        retrofit.create(EBirdApiService::class.java)
    }

    // Function to get recent bird observations. This function can only be called from
    // another 'suspend' function or from a coroutine.
    suspend fun getRecentObservations(languageCode: String): List<Bird>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = eBirdApiService.getRecentObservations(languageCode, 4)
                if (response.isSuccessful) {
                    response.body()?.map { mapToBird(it, languageCode) }
                } else {
                    println("Error: ${response.code()} - ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
                null
            }
        }
    }

    private suspend fun getBirdInformation(sighting: SimpleBirdSighting, languageCode: String): Result<SimpleWikipediaResponse?> {
        val api = WikipediaApi(languageCode)
        return api.getAdditionalBirdInfo(sighting.sciName)
    }

    // Function used to map SimpleBirdSighting to a Bird object, enriched with additional
    // information from Wikipedia.
    private suspend fun mapToBird(sighting: SimpleBirdSighting, languageCode: String): Bird {
        val additionalInfo = getBirdInformation(sighting, languageCode)

        return Bird(
            speciesName = sighting.comName,
            speciesNameScientific = sighting.sciName,
            description = when (additionalInfo) {
                is Result.Success -> additionalInfo.value?.extract
                is Result.Failure -> "Failed to fetch bird description: ${additionalInfo.message}."
            },
            photoUrl = when (additionalInfo) {
                is Result.Success -> additionalInfo.value?.thumbnail
                is Result.Failure -> "Failed to fetch thumbnail: ${additionalInfo.message}"
            }
        )
    }
}