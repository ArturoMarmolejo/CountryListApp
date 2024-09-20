package com.arturomarmolejo.countrylistapp.presentation.views

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arturomarmolejo.countrylistapp.R
import com.arturomarmolejo.countrylistapp.data.network.NetworkModule
import com.arturomarmolejo.countrylistapp.data.repository.CountryRepository
import com.arturomarmolejo.countrylistapp.data.repository.CountryRepositoryImpl
import com.arturomarmolejo.countrylistapp.databinding.FragmentCountryListBinding
import com.arturomarmolejo.countrylistapp.presentation.UIState
import com.arturomarmolejo.countrylistapp.presentation.viewmodel.CountryViewModel
import com.arturomarmolejo.countrylistapp.presentation.views.recyclerview.adapter.CountryListAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [CountryListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CountryListFragment : Fragment() {

    private var _binding: FragmentCountryListBinding? = null
    private val binding get() = _binding!!


    private val recyclerView: RecyclerView by lazy {
        binding.countryListRecyclerview
    }
    private var scrollPosition: Parcelable? = null

    /**
     * Instance of [CountryViewModel] to be used by the fragment
     * Notice that since we don't make use of any dependency injection framework, we have to
     * provide additional instances for the repository and the coroutine dispatcher, and the
     * service provider to perform the network call
     */
    private val countryViewModel: CountryViewModel by lazy {
        val service = NetworkModule.providesCountryService()
        val dispatcher = NetworkModule.providesCoroutineDispatcher()
        val repository: CountryRepository = CountryRepositoryImpl(service, dispatcher)
        CountryViewModel(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment. Notice use of ViewBinding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val listAdapter = CountryListAdapter()
        recyclerView.adapter = listAdapter

        //Set layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //Set state restoration policy to save scroll position and make survive through configuration changes
        listAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        listAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (scrollPosition != null) {
                    recyclerView.layoutManager?.onRestoreInstanceState(scrollPosition)
                    scrollPosition = null
                }
            }
        })



        viewLifecycleOwner.lifecycleScope.launch {
            //Observation of flow data stream from the ViewModel. Notice use of repeatOnLifecycle
            //to make the flow only stream as long as the fragment is active.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                countryViewModel.allCountries.collect { state ->
                    when (state) {
                        is UIState.LOADING -> {
                            //Loading spinner to be visible when we receive the first state
                            binding.loadingSpinner.visibility = View.VISIBLE
                        }
                        is UIState.SUCCESS -> {
                            binding.loadingSpinner.visibility = View.GONE
                            //Submit list to the adapter when we receive a success state
                            listAdapter.submitList(state.response)
                        }
                        is UIState.ERROR -> {
                            binding.loadingSpinner.visibility = View.GONE
                            //Toast to be shown when we receive an error state
                            Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_LONG).show()
                        }
                    }

                }
            }
        }

        //Listener for the scroll event to save the scroll position from the recycler view
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollPosition = recyclerView.layoutManager?.onSaveInstanceState()
            }
        })

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        scrollPosition = binding.countryListRecyclerview.layoutManager?.onSaveInstanceState()
        _binding = null
    }
}