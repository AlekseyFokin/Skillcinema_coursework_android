package org.sniffsnirr.skillcinema.ui.start

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.onboarding.OnboardingMainViewModel

class StartFragment : Fragment() {

    private val viewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      //  findNavController().clearBackStack(R.id.startFragment)
       // findNavController().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}