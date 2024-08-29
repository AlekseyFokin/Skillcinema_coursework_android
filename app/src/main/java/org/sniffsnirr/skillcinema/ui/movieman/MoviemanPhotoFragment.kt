package org.sniffsnirr.skillcinema.ui.movieman

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentMoviemanPhotoBinding

class MoviemanPhotoFragment : Fragment() {

    private var _binding:FragmentMoviemanPhotoBinding?=null
    val binding get()=_binding!!

    private var photoUrl=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoUrl=arguments?.getCharSequence(MoviemanFragment.PHOTO_URL,"").toString()
       }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding=FragmentMoviemanPhotoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide
            .with(binding.moviemanPhoto.context)
            .load(photoUrl)
            .into(binding.moviemanPhoto)

    }
}