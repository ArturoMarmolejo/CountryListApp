package com.arturomarmolejo.countrylistapp.presentation.views.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.arturomarmolejo.countrylistapp.data.model.CountryResponseItem
import com.arturomarmolejo.countrylistapp.databinding.CountryItemBinding


/**
 * [CountryListViewHolder] - class used to bind the data to the view elements in layout
 */
class CountryListViewHolder(
    private val binding: CountryItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(country: CountryResponseItem) {
        binding.countryNameRegion.text = "${country.name}, ${country.region}"
        binding.countryCode.text = country.code
        binding.countryCapital.text = country.capital
    }
}