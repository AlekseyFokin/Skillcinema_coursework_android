package org.sniffsnirr.skillcinema.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
// адаптер для viewpager
class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return NUMBER_OF_PAGE
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoarding0Fragment()
            1 -> OnBoarding1Fragment()
            else -> OnBoarding2Fragment()
        }
    }

    companion object {
        const val NUMBER_OF_PAGE = 3
    }
}
