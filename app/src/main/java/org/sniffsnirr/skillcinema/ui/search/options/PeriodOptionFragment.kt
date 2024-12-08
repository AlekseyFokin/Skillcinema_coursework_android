package org.sniffsnirr.skillcinema.ui.search.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.databinding.FragmentPeriodOptionBinding
import org.sniffsnirr.skillcinema.ui.exception.BottomSheetErrorFragment

@AndroidEntryPoint
class PeriodOptionFragment : Fragment() {

    private val viewModel: AllOptionsViewModel by viewModels({ requireParentFragment() })
    var _binding: FragmentPeriodOptionBinding? = null
    val binding get() = _binding!!
    private var startYear: Int? = null
    private var endYear: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showActionBar()

        binding.startPeriod.addOutYearListeners { year -> getStartYear(year) }

        binding.endPeriod.addOutYearListeners { year -> getEndYear(year) }

        binding.goBtn.setOnClickListener {//проверка
            if (startYear != null && endYear != null) {
                if (startYear!! > endYear!!) {
                    viewModel.setStartPeriod(null)
                    viewModel.setEndPeriod(null)
                    Toast.makeText(
                        requireContext(),
                        "Начало периода позже конца периода. Данные не приняты",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            parentFragmentManager.popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {// ожидание ошибки
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.take(1).collect { _ ->
                    BottomSheetErrorFragment().show(parentFragmentManager, "errordialog")
                }
            }
        }
    }

    private fun getStartYear(year: Int?) {
        startYear = year
        viewModel.setStartPeriod(year)
    }

    private fun getEndYear(year: Int?) {
        endYear = year
        viewModel.setEndPeriod(year)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle(NAME_FRAGMENT)
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeriodOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val NAME_FRAGMENT = "Период"
    }


}