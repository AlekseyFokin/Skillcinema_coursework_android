package org.sniffsnirr.skillcinema.ui.exception

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sniffsnirr.skillcinema.databinding.FragmentExceptionBinding
import kotlin.system.exitProcess


class BottomSheetErrorFragment  : BottomSheetDialogFragment() {

   private var _binding: FragmentExceptionBinding?=null
    val binding get() = _binding!!

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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(Build.VERSION.SDK_INT in 16..20){
            activity?.finishAffinity()
            exitProcess(0)
        } else if(Build.VERSION.SDK_INT>=21){
            activity?.finishAndRemoveTask()
            exitProcess(0)
        }
    }
}