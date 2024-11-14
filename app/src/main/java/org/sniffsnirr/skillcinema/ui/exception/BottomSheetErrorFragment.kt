package org.sniffsnirr.skillcinema.ui.exception

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentExceptionBinding


class BottomSheetErrorFragment  : BottomSheetDialogFragment() {

   private var _binding: FragmentExceptionBinding?=null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentExceptionBinding.inflate(layoutInflater,container,false)
         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeBtn.setOnClickListener {  dismiss() }
    }

}