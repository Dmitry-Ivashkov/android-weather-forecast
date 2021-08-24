package com.example.weather

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.weather.PagesWeatherFragment.Companion.defaultList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val model: CityModel
) : ViewModel() {

    private var cityNames: Array<String>? = null
    private var cityIds: Array<Int>? = null

    @ExperimentalCoroutinesApi
    val listData: StateFlow<List<Data>> = model.city.flatMapLatest { _city ->
        getCityWeather(cityNames, cityIds, _city)
    }.stateIn(GlobalScope, SharingStarted.Eagerly, defaultList)

    fun setCityNamesAndIds(
        _cityNames: Array<String>?,
        _cityIds: Array<Int>?
    ) {
        cityNames = _cityNames
        cityIds = _cityIds
    }

    fun setCity(city: String?) {
        model.setCity(city)
    }


    private fun getCityWeather(
        cityNames: Array<String>,
        cityIds: Array<Int>,
        city: String
    ): Flow<List<Data>> {
        return flow {
            val cityId = cityIds[cityNames.indexOf(city)]
            val response = networkManager.getCityWeather(NetworkManager.REQUEST_BY_CITY_ID, cityId)
            val allWeather = if (response.isSuccessful) response.body() else null
            val days =
                allWeather?.list?.filter { listItem -> listItem.dtTxt.contains(Regex("13:00:00")) }
            var weatherInDataFormat = days?.map { item ->
                Data(
                    temp = item.main.temp.toCelsius(),
                    weather = item.weather.getOrNull(0)?.main ?: NAN,
                    city = city,
                    item.dt.getDayOfWeek()
                )
            } ?: listOf()

            if (weatherInDataFormat.isEmpty()) {
                weatherInDataFormat = defaultList
            }

            emit(weatherInDataFormat)
        }
    }

    @JvmName("getCityWeather1")
    private fun getCityWeather(
        cityNames: Array<String>?,
        cityIds: Array<Int>?,
        city: String?
    ): Flow<List<Data>> = cityNames?.let { cityNames_ ->
        cityIds?.let { CityIds_ ->
            city?.let { city_ ->
                this@ViewModel.getCityWeather(
                    cityNames_,
                    CityIds_,
                    city_
                )
            }
        }
    }
        ?: flow { emit(defaultList) }

    companion object {
        private const val NAN = "NAN"

        private fun Double.toCelsius(): Int {
            return (this - 273.15).toInt()
        }

        private fun Long.getDayOfWeek(): String {
            val c = GregorianCalendar()
            c.time = Time(this * 1000)
            return SimpleDateFormat("EEEE").format(c.time)
        }
    }
}


