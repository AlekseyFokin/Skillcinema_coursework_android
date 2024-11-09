package org.sniffsnirr.skillcinema.ui.search.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sniffsnirr.skillcinema.databinding.SearchOptionRvItemBinding
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Country

class CountryOptionAdapter(val onCountryClick: (Country) -> Unit) :
    RecyclerView.Adapter<CountryOptionAdapter.CountryViewHolder>() {

    var  countries= emptyList<Country>()
    var selectedItemPosition = RecyclerView.NO_POSITION

    fun setCountryList(newCountryList:List<Country>){
        this.countries=newCountryList
        notifyDataSetChanged()
    }

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

        holder.itemView.setSelected(selectedItemPosition == position)
        holder.itemView.setOnClickListener{
            onCountryClick(country)
            val previousSelectedItemPosition = selectedItemPosition
            selectedItemPosition = holder.layoutPosition
            notifyItemChanged(previousSelectedItemPosition)
            notifyItemChanged(selectedItemPosition)
        }

    }
}