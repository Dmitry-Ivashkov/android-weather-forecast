package com.example.weather.managers

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.support.annotation.RequiresApi
import com.example.weather.dataClasses.AllWeather
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject


class NetworkManager @Inject constructor(@ApplicationContext val applicationContext: Context) {
    companion object {
        const val REQUEST_BY_CITY_ID = 1
    }
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun isConected(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    object RetrofitClient {
        private var retrofit: Retrofit? = null

        fun getClient(baseUrl: String): Retrofit =
            retrofit?.let {
                retrofit as Retrofit
            } ?: Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    interface RetrofitServicesByCityId {
        @GET("hourly?appid=b1b15e88fa797225412429c1c50c122a1")
        fun getCityWeather(@Query("id") cityId: Int): Call<AllWeather>
    }

    object CommonByCityId {
        private val BASE_URL = "https://pro.openweathermap.org/data/2.5/forecast/"
        val retrofitServiceByCityId: RetrofitServicesByCityId
            get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServicesByCityId::class.java)
    }

    fun getCityWeather(id: Int, cityId: Int): Response<AllWeather> {
        return when (id) {
            REQUEST_BY_CITY_ID -> CommonByCityId.retrofitServiceByCityId.getCityWeather(cityId)
                .execute()
            else -> Response.error(2, ResponseBody.create(null, ""))
        }
    }
}