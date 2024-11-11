package org.sniffsnirr.skillcinema.ui.search.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentPeriodOptionBinding
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel
import org.sniffsnirr.skillcinema.ui.search.options.AllOptionsFragment.Companion

@AndroidEntryPoint
class PeriodOptionFragment : Fragment() {

    private val viewModel: AllOptionsViewModel by viewModels({requireParentFragment()})
    var _binding :FragmentPeriodOptionBinding?=null
    val binding get()=_binding!!
    var startYear:Int?=null
    var endYear:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startPeriod.addOutYearListeners {year->getStartYear(year)}

        binding.endPeriod.addOutYearListeners {year->getEndYear(year)}

         binding.goBtn.setOnClickListener {//проверка
            if (startYear!=null&&endYear!=null) {
                if (startYear!! >endYear!!) {
                    viewModel.setStartPeriod(null)
                    viewModel.setEndPeriod(null)
                    Toast.makeText(
                        requireContext(),
                        "Начало периода позже конца периода. Данные не приняты",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
             parentFragmentManager.popBackStack();
         }
    }

    fun getStartYear(year:Int?){
        startYear=year
        viewModel.setStartPeriod(year)
    }

    fun getEndYear(year:Int?){
        endYear=year
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

    override fun onDestroy() {
        super.onDestroy()
     //   (activity as MainActivity).hideActionBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
_binding=FragmentPeriodOptionBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object{
        const val NAME_FRAGMENT="Период"
    }


}