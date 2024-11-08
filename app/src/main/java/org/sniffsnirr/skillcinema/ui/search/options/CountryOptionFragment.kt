package org.sniffsnirr.skillcinema.ui.search.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentCountryOptionBinding
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel
import org.sniffsnirr.skillcinema.ui.search.options.AllOptionsFragment.Companion

@AndroidEntryPoint
class CountryOptionFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels({requireParentFragment()})
    var _binding:FragmentCountryOptionBinding?=null
    val binding get()=_binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
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
        (activity as MainActivity).hideActionBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
_binding=FragmentCountryOptionBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object{
        const val NAME_FRAGMENT="Страна"
    }

}