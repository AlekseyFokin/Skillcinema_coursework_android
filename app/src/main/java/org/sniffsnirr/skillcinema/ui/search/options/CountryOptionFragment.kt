package org.sniffsnirr.skillcinema.ui.search.options

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentCountryOptionBinding
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Country
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel
import org.sniffsnirr.skillcinema.ui.search.options.AllOptionsFragment.Companion

@AndroidEntryPoint
class CountryOptionFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels({requireParentFragment()})
    var _binding:FragmentCountryOptionBinding?=null
    val binding get()=_binding!!
    private val countryAdapter=CountryOptionAdapter{country->onCountryClick(country)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.countryRv.setHasFixedSize(true)
        binding.countryRv.adapter =countryAdapter
        val dividerItemDecorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        dividerItemDecorator.setDrawable(resources.getDrawable(R.drawable.line_for_rv))
        binding.countryRv.addItemDecoration(dividerItemDecorator)

        viewLifecycleOwner.lifecycleScope.launch {// загрузка списка стран
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countries.collect {
                    countryAdapter.setCountryList(it)
                }
            }
        }

        binding.searchView.searchTextInput.addTextChangedListener {
            viewModel.setCountrySearchString(binding.searchView.searchTextInput.text.toString())
            viewModel.onChangeCountrySearchString()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle(NAME_FRAGMENT)
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    override fun onDestroy() {
        super.onDestroy()
    //    (activity as MainActivity).hideActionBar()
        viewModel.setCountrySearchString("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
_binding=FragmentCountryOptionBinding.inflate(inflater,container,false)
        return binding.root
    }

    fun onCountryClick(selectedCountry:Country){
        viewModel.setCountry(selectedCountry)
    }

    companion object{
        const val NAME_FRAGMENT="Страна"
    }

}