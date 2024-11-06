package org.sniffsnirr.skillcinema.ui.search.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentCountryOptionBinding

class CountryOptionFragment : Fragment() {
    var _binding:FragmentCountryOptionBinding?=null
    val binding get()=_binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
_binding=FragmentCountryOptionBinding.inflate(inflater,container,false)
        return binding.root
    }

}