package org.sniffsnirr.skillcinema.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sniffsnirr.skillcinema.databinding.FragmentOnBoarding0Binding
@AndroidEntryPoint
class OnBoarding0Fragment : Fragment() {

    private var _binding: FragmentOnBoarding0Binding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingMainViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoarding0Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.onboarding0Miss.setOnClickListener {
            viewModel.setState(OnboardingMainViewModel.ONBORDING_1_FRAGMENT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}