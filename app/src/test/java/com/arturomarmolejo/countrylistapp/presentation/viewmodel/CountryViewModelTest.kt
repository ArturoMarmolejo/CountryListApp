package com.arturomarmolejo.countrylistapp.presentation.viewmodel

import android.provider.Contacts.Intents.UI
import com.arturomarmolejo.countrylistapp.data.model.CountryResponseItem
import com.arturomarmolejo.countrylistapp.data.model.Currency
import com.arturomarmolejo.countrylistapp.data.repository.CountryRepository
import com.arturomarmolejo.countrylistapp.presentation.UIState
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CountryViewModelTest {

    private lateinit var countryViewModel: CountryViewModel
    private lateinit var repository: CountryRepository


    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mockk<CountryRepository>(relaxed = true)
        countryViewModel = CountryViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }


    @Test
    fun `getAllCountries updates allCountries state flow with repository data`() = runTest {
        val countryList = listOf(
            CountryResponseItem("name1", "capital1",  Currency("currency"), "flag1"),
            CountryResponseItem("name2", "capital2",  Currency("currency"), "flag2")
        )

        val flow = flowOf(
            UIState.LOADING,
            UIState.SUCCESS(countryList)
        )

        coEvery { repository.getAllCountries() } returns flow

        countryViewModel.getAllCountries()
        countryViewModel.allCountries.drop(1).first()

        assertEquals(UIState.SUCCESS(countryList), countryViewModel.allCountries.value)
    }

    @Test
    fun `getAllCountries handles error state from repository`() = runTest {
        val exception = Exception("Network error")
        val flow = flow { emit(UIState.ERROR(exception))  }

        coEvery { repository.getAllCountries() } returns flow as Flow<UIState<List<CountryResponseItem>?>>?


        countryViewModel.getAllCountries()
        countryViewModel.allCountries.drop(1).first()
        advanceUntilIdle()
        assertEquals(countryViewModel.allCountries.value, UIState.ERROR(exception))

    }
}