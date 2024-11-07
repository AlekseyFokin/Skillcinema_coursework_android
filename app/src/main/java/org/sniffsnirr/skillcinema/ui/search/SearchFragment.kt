package org.sniffsnirr.skillcinema.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentSearchBinding
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.searchOptions.setOnClickListener {
            findNavController().navigate(
            R.id.action_navigation_search_to_allOptionsFragment
        ) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}