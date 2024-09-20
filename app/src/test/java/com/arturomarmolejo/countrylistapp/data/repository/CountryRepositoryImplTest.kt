package com.arturomarmolejo.countrylistapp.data.repository

import com.arturomarmolejo.countrylistapp.data.model.CountryResponseItem
import com.arturomarmolejo.countrylistapp.data.model.Currency
import com.arturomarmolejo.countrylistapp.data.network.CountryServiceApi
import com.arturomarmolejo.countrylistapp.presentation.UIState
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Response.*

class CountryRepositoryImplTest {

    private lateinit var repository: CountryRepository
    private lateinit var service: CountryServiceApi

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        service = mockk<CountryServiceApi>()
        repository = CountryRepositoryImpl(service, dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }


    @Test
    fun `getAllCountries emits LOADING state first`() = runTest {
        val flow = repository.getAllCountries()
        val firstEmission = flow?.first()
        assertEquals(UIState.LOADING, firstEmission)
    }

    @Test
    fun `getAlCountries emits SUCCESS state with data when response is successful`() = runTest {
        val countryList = listOf(
            CountryResponseItem("name1", "capital1",  Currency("currency"), "flag1"),
            CountryResponseItem("name2", "capital2",  Currency("currency"), "flag2")
        )

        val response = success(countryList)
        coEvery { service.getAllCountries() } returns response

        val flow = repository.getAllCountries()
        val emissions = flow?.toList()

        assertEquals(2, emissions?.size)
        assertEquals(UIState.LOADING, emissions?.get(0))
        assertEquals(UIState.SUCCESS(countryList), emissions?.get(1))
    }

    @Test
    fun `getAllCountries emits ERROR State when response is NOT Successful`() = runTest {
        val response = Response.error<List<CountryResponseItem>>(400, "".toResponseBody())

        coEvery { service.getAllCountries() } returns response

        val flow = repository.getAllCountries()
        val emissions = flow?.toList()

        assertEquals(2, emissions?.size)
        assertEquals(UIState.LOADING, emissions?.get(0))
        assertTrue(emissions?.get(1) is UIState.ERROR)
    }

    @Test
    fun `getAllCountries emits ERROR state when response body is null`() = runTest {
        val response = Response.success<List<CountryResponseItem>?>(200, null)
        coEvery { service.getAllCountries() } returns response

        val flow = repository.getAllCountries()
        val emissions = flow?.toList()

        assertEquals(2, emissions?.size)
        assertEquals(UIState.LOADING, emissions?.get(0))
        assertTrue(emissions?.get(1) is UIState.ERROR)
    }

    @Test
    fun `getAllCountries emits error when exception is thrown`() = runTest {
        coEvery { service.getAllCountries() } throws RuntimeException("Network error")

        val flow = repository.getAllCountries()
        val emissions = flow?.toList()

        assertEquals(2, emissions?.size)
        assertEquals(UIState.LOADING, emissions?.get(0))
        assertTrue(emissions?.get(1) is UIState.ERROR)
    }

}