package com.arturomarmolejo.countrylistapp.data.repository

import com.arturomarmolejo.countrylistapp.data.model.CountryResponseItem
import com.arturomarmolejo.countrylistapp.data.network.CountryServiceApi
import com.arturomarmolejo.countrylistapp.presentation.UIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

/**
 * [CountryRepository] -
 * Defines the methods to get the response from the API asynchronously using suspend functions
 */
interface CountryRepository {
    suspend fun getAllCountries(): Flow<UIState<List<CountryResponseItem>?>>?
}

/**
 * [CountryRepositoryImpl] -
 * Implementation of [CountryRepository] interface
 * @param countryServiceApi defines the API interface to be called in order to get the response
 * @param coroutineDispatcher defines the thread to run the coroutine on. We use the IO thread
 * since it is an operation to be performed in the background
 */
class CountryRepositoryImpl(
    private val countryServiceApi: CountryServiceApi,
    private val coroutineDispatcher: CoroutineDispatcher
): CountryRepository {

    /**
     * [getAllCountries] -
     * Gets List of Countries information from the API asynchronously using a suspend function,
     * @return the state of the country list information in a Flow. Server response error handling
     * happens here
     */
    override suspend fun getAllCountries(): Flow<UIState<List<CountryResponseItem>?>> = flow {
        emit(UIState.LOADING)
        try {
            val response = countryServiceApi.getAllCountries()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(UIState.SUCCESS(it))
                } ?: throw Exception("Response from server is null")
            } else {
                throw Exception(response.errorBody()?.string())
            }
        } catch (exception: Exception) {
            emit(UIState.ERROR(exception))
        }
    }.flowOn(coroutineDispatcher)

}