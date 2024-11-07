package org.sniffsnirr.skillcinema.ui.search.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentAllOptionsBinding

@AndroidEntryPoint
class AllOptionsFragment : Fragment() {

    var _binding: FragmentAllOptionsBinding? = null
    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showActionBar()
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
    ): View {
        _binding = FragmentAllOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gotoCountryOption.setOnClickListener {
            findNavController().navigate(
                R.id.action_allOptionsFragment_to_countryOptionFragment
            )
        }

        binding.gotoGenreOption.setOnClickListener {
            findNavController().navigate(
                R.id.action_allOptionsFragment_to_genreOptionFragment2
            )
        }

        binding.gotoYearOption.setOnClickListener {
            findNavController().navigate(
                R.id.action_allOptionsFragment_to_periodOptionFragment
            )
        }
    }

    companion object{
        const val NAME_FRAGMENT="Настройки поиска"
    }

}