package org.sniffsnirr.skillcinema.ui.serial

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentSerialSeasonBinding

class SerialSeasonFragment : Fragment() {

    private val viewModel: SerialSeasonViewModel by viewModels()
var _binding:FragmentSerialSeasonBinding?=null
    val binding get()=_binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentSerialSeasonBinding.inflate(inflater,container,false)
        return binding.root
    }
}