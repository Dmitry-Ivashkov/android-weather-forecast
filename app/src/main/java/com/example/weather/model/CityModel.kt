package com.example.weather.model

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CityModel @Inject constructor() {
    fun setCity(_city: String?) {
        city.value = _city
    }

    val city: MutableStateFlow<String?> = MutableStateFlow(null)
}