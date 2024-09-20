package com.arturomarmolejo.countrylistapp.data.network

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * [NetworkModule]
 * singleton used to create a Retrofit instance for handling network call to the API,
 * along with a coroutine dispatcher.
 * Best practice is to do this using a DI framework such as Dagger and Hilt
 */
object NetworkModule {

    private val gson: Gson = Gson()

    private val retrofit = Retrofit.Builder()
        .baseUrl(CountryServiceApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    /**
     * [providesCountryService] - creates an instance of the [CountryServiceApi] to be used by
     * the [CountryRepositoryImpl]
     */
    fun providesCountryService(): CountryServiceApi {
        return retrofit.create(CountryServiceApi::class.java)
    }


    /**
     * [providesCoroutineDispatcher] - creates an instance of the [CoroutineDispatcher] to be used by
     * the [CountryRepositoryImpl] and run the coroutine on the IO thread
     */
    fun providesCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}