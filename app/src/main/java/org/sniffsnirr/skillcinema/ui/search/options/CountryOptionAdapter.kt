package org.sniffsnirr.skillcinema.ui.search.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.databinding.SearchOptionRvItemBinding
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Country
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Genre

class CountryOptionAdapter(val  countries: List<Country>) :
    RecyclerView.Adapter<CountryOptionAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(val binding: SearchOptionRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =SearchOptionRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CountryViewHolder(binding)
    }

    override fun getItemCount()=countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.binding.itemText.text=country.country
    }
}