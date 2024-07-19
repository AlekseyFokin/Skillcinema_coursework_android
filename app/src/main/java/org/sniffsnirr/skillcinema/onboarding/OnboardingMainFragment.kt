package org.sniffsnirr.skillcinema.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentOnboardingMainBinding
import java.util.Timer
import java.util.TimerTask


class OnboardingMainFragment : Fragment() {

    private val viewModel: OnboardingMainViewModel by viewModels()

    private var _binding: FragmentOnboardingMainBinding? = null
    private val binding get() = _binding!!
    private val timer: Timer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingMainBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.buttonToLoading.setOnClickListener {
        //    (activity as MainActivity).showBars()
        //     findNavController().navigate(R.id.action_onboardingMainFragment_to_startFragment)
        // }

        binding.onboardingViewPager.adapter = PagerAdapter(this)
        binding.tabs.tabIconTint = null

        TabLayoutMediator(binding.tabs, binding.onboardingViewPager) { tab, pos ->
            when (pos) {
                0 -> tab.setIcon(R.drawable.onboarding_label_black)
                1 -> tab.setIcon(R.drawable.onboarding_label_gray)
                2 -> tab.setIcon(R.drawable.onboarding_label_gray)
            }
        }.attach()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.setIcon(R.drawable.onboarding_label_black)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.setIcon(R.drawable.onboarding_label_gray)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })


        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                binding.tabs.post(Runnable {
                    var currentPage: Int = binding.tabs.selectedTabPosition
                    currentPage++
                    if (currentPage >= binding.tabs.tabCount) {
                        findNavController().navigate(R.id.action_onboardingMainFragment_to_startFragment)
                        (activity as MainActivity).showBars()
                        swipeTimer.cancel()}
                    else{binding.tabs.selectTab(binding.tabs.getTabAt(currentPage))}

                })
            }
        }, 5000, 5000) // Change the delay and interval as needed
    }

override fun onDestroy() {
    super.onDestroy()
    timer?.cancel()
}
}