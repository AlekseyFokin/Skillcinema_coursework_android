package org.sniffsnirr.skillcinema.ui.onemovie

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sniffsnirr.skillcinema.R

class OneMovieFragment : Fragment() {

    companion object {
        fun newInstance() = OneMovieFragment()
    }

    private val viewModel: OneMovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_one_movie, container, false)
    }
}