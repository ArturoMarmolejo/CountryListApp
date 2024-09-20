package com.arturomarmolejo.countrylistapp.presentation.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturomarmolejo.countrylistapp.data.model.CountryResponseItem
import com.arturomarmolejo.countrylistapp.data.repository.CountryRepository
import com.arturomarmolejo.countrylistapp.presentation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * [CountryViewModel] -
 * Defines the ViewModel for the application
 * Contains the information that may be shared between different composable functions
 * @param countryRepository defines the repository to be used by the ViewModel in order to retrieve
 * data from the data layer
 */
class CountryViewModel(
    private val countryRepository: CountryRepository,
): ViewModel() {

    private val _allCountries: MutableStateFlow<UIState<List<CountryResponseItem>?>> = MutableStateFlow(UIState.LOADING)
    val allCountries: StateFlow<UIState<List<CountryResponseItem>?>> get() = _allCountries

    init {
        getAllCountries()
    }

    /**
     * [getAllCountries] -
     * Retrieves the API data stream from the repository and saves it in a mutable state to be used
     * by the views in this layer
     */
    @VisibleForTesting
    internal fun getAllCountries() {
        viewModelScope.launch {
            countryRepository.getAllCountries()?.collect {
                _allCountries.value = it
            }
        }
    }
}