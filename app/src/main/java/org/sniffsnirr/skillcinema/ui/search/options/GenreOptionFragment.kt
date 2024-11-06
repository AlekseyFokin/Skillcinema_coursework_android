package org.sniffsnirr.skillcinema.ui.search.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentGenreOptionBinding

class GenreOptionFragment : Fragment() {

    var _binding:FragmentGenreOptionBinding?= null
    val binding get()=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {_binding=FragmentGenreOptionBinding.inflate(inflater,container,false)
        return binding.root
    }


}