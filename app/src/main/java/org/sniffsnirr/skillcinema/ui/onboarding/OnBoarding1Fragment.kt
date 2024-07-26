package org.sniffsnirr.skillcinema.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.sniffsnirr.skillcinema.databinding.FragmentOnBoarding1Binding

class OnBoarding1Fragment : Fragment() {

    private var _binding: FragmentOnBoarding1Binding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingMainViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoarding1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.onboarding1Miss.setOnClickListener {
            viewModel.setState(OnboardingMainViewModel.ONBORDING_2_FRAGMENT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}