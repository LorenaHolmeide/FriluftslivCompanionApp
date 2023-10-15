package no.hiof.friluftslivcompanionapp.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.data.states.TabsUiState
import no.hiof.friluftslivcompanionapp.data.states.WeatherState
import no.hiof.friluftslivcompanionapp.domain.WeatherDeserialiser
import no.hiof.friluftslivcompanionapp.models.WeatherForecast
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.models.enums.WeatherUnit
import no.hiof.friluftslivcompanionapp.models.interfaces.TabNavigation
import javax.inject.Inject

// NOTE: Composable Screens in app/ui/screens can communicate with this viewmodel (and thus the data
// layer via 'import androidx.lifecycle.viewmodel.compose.viewModel' at the top of the file, and
// then adding 'viewModel: TripsViewModel = viewModel()' in the constructor.
// Afterwards, go to the relevant activity (in this case MainActivity), navigate to the NavHost
// section where all navigation routes are defined, and update it to something like this:
//composable(Screen.TRIPS.name) { backStackEntry ->
//    val viewModel = hiltViewModel<TripsViewModel>()
//    TripsScreen(navController, modifier.padding(innerPadding), viewModel)
//}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    // Communication with the data layer can be injected as dependencies here.
    // private val repository: TripsRepository
) : ViewModel(), TabNavigation {

    private val _uiState = MutableStateFlow(TabsUiState())
    override val uiState: StateFlow<TabsUiState> = _uiState.asStateFlow()

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val api = WeatherDeserialiser.getInstance()

    // Tab destinations that are accessible in the "weather" route.
    override var tabDestinations = mapOf(
        Screen.WEATHER to "Weather",
        Screen.WEATHER_SEARCH to "Search"
    )

    // Options available to the Bottom Sheet radio buttons.
    val radioOptions = mapOf(
        WeatherUnit.METRIC to "Metric (°C & m/s)",
        WeatherUnit.IMPERIAL to "Imperial (°F & mi/s)",
        WeatherUnit.DEFAULT to "Default (K & m/s)"
    )

    // Retrieves a WeatherForecast object composed of a Location and a List<Weather> wrapped by
    // a Result.
    suspend fun getWeatherForecast(lat: Double?, lon: Double?) {
        // Makes sure to reset failure state to false so request can be retried.
        updateFailureWeatherResponse(false)
        updateNoGps(false)
        // Changes loading state to true - shows progress bar.
        updateLoadingWeatherResponse(true)

        if (lat == null && lon == null) {
            updateNoGps(true)
        } else {
            // Retrieves API response asynchronously.
            val result = api.getWeatherForecast(
                lat ?: DefaultLocation.OSLO.lat,
                lon ?: DefaultLocation.OSLO.lon,
                units = weatherState.value.unitChoice
            )

            if (result is Result.Success) {
                // Updates individual Weather objects from a single WeatherForecast
                updateWeatherState(result.value)
            } else {
                updateFailureWeatherResponse(true)
            }
        }

        // Changes loading state to false.
        updateLoadingWeatherResponse(false)
    }

    // Updates tab selection visualisation after navigation.
    override fun changeHighlightedTab(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentTabIndex = index
            )
        }
    }

    // Function to retrieve the last highlighted tab.
    override fun getHighlightedTab(): Int {
        return  _uiState.value.currentTabIndex
    }

    // Updates each individual daily forecast from a List<Weather> found in a WeatherForecast.
    fun updateWeatherState(weatherForecast: WeatherForecast) {
        _weatherState.update { currentState ->
            currentState.copy(
                currentWeather = weatherForecast.forecast[0],
                todayWeather = weatherForecast.forecast[1],
                todayPlusOneWeather = weatherForecast.forecast[2],
                todayPlusTwoWeather = weatherForecast.forecast[3],
                todayPlusThreeWeather = weatherForecast.forecast[4],
                todayPlusFourWeather = weatherForecast.forecast[5],
                todayPlusFiveWeather = weatherForecast.forecast[6],
                todayPlusSixWeather = weatherForecast.forecast[7],
                todayPlusSevenWeather = weatherForecast.forecast[8]
            )
        }
    }

    // Changes a Boolean value used to show/hide a progress bar.
    fun updateLoadingWeatherResponse(isLoading: Boolean) {
        _weatherState.update { currentState ->
            currentState.copy(
                isLoading = isLoading
            )
        }
    }

    // Changes a Boolean value used to show/hide error messages.
    fun updateFailureWeatherResponse(isFailure: Boolean) {
        _weatherState.update { currentState ->
            currentState.copy(
                isFailure = isFailure
            )
        }
    }

    // Changes a Boolean value used to show/hide error messages.
    fun updateNoGps(isNoGps: Boolean) {
        _weatherState.update { currentState ->
            currentState.copy(
                isNoGps = isNoGps
            )
        }
    }

    // Updates the preferred weather unit format.
    fun updateWeatherUnit(weatherUnit: WeatherUnit) {
        _weatherState.update { currentState ->
            currentState.copy(
                unitChoice = weatherUnit
            )
        }
    }
}