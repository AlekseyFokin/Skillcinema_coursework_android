package org.sniffsnirr.skillcinema.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentHomeBinding
import org.sniffsnirr.skillcinema.ui.home.adapter.MainAdapter

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
   // private val viewModel: HomeViewModel by navGraphViewModels(R.id.mobile_navigation)
   private val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.mobile_navigation)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainRv.setHasFixedSize(true)


        viewLifecycleOwner.lifecycleScope.launch {// загрузка премьер
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.premiers.collect {
                    binding.mainRv.adapter=MainAdapter(it)
                    //binding.textHome.text = it.size.toString()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}