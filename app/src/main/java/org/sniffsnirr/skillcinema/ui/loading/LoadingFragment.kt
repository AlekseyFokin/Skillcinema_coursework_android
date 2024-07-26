package org.sniffsnirr.skillcinema.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentLoadingBinding
import org.sniffsnirr.skillcinema.ui.home.HomeViewModel

class LoadingFragment : Fragment() {

    private val viewModel: HomeViewModel by navGraphViewModels(R.id.mobile_navigation)
    private var _binding: FragmentLoadingBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isLoading.onEach {//отслеживание загрузки премьер
            if (it) {
                binding.loadingProgressbar.visibility = View.VISIBLE
            } else {
                binding.loadingProgressbar.visibility = View.GONE
                findNavController().navigate(R.id.action_loadingFragment_to_navigation_home)
                (activity as MainActivity).showBars()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

}