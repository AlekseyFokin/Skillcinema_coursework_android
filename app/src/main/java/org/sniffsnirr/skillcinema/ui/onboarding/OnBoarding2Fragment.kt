package org.sniffsnirr.skillcinema.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sniffsnirr.skillcinema.databinding.FragmentOnBoarding2Binding
@AndroidEntryPoint
class OnBoarding2Fragment : Fragment() {

    private var _binding: FragmentOnBoarding2Binding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingMainViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoarding2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.onboarding2Miss.setOnClickListener {
            viewModel.setState(OnboardingMainViewModel.LOADING_FRAGMENT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}