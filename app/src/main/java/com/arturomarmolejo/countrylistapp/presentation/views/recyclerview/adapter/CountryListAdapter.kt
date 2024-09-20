package com.arturomarmolejo.countrylistapp.presentation.views.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.arturomarmolejo.countrylistapp.data.model.CountryResponseItem
import com.arturomarmolejo.countrylistapp.databinding.CountryItemBinding
import com.arturomarmolejo.countrylistapp.presentation.views.recyclerview.viewholder.CountryListViewHolder

/**
 * [CountryListAdapter] - ListAdapter used to display the list of countries in a RecyclerView.
 * Notice that we use this instead of the regular Adapter class in order to allow the data to be
 * updated asynchronously using DiffUtil.
 */
class CountryListAdapter: ListAdapter<
        CountryResponseItem, CountryListViewHolder>(CountryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryListViewHolder {
        return CountryListViewHolder(
            CountryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CountryListViewHolder, position: Int) {
        val country = getItem(position)
        holder.bind(country)
    }

}

class CountryDiffCallback: DiffUtil.ItemCallback<CountryResponseItem>() {

    override fun areItemsTheSame(
        oldItem: CountryResponseItem,
        newItem: CountryResponseItem
    ): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(
        oldItem: CountryResponseItem,
        newItem: CountryResponseItem
    ): Boolean {
        return oldItem == newItem
    }
}