package org.sniffsnirr.skillcinema.ui.onemovie.gallery

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentGalleryBinding
import org.sniffsnirr.skillcinema.ui.exception.BottomSheetErrorFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val viewModel: GalleryViewModel by viewModels()
    private var _binding: FragmentGalleryBinding? = null
    val binding get() = _binding!!
    private var movieId = 0
    private val listOfPhotoUrl = ArrayList<String>()
    private lateinit var pagedAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = arguments?.getInt(ID_MOVIE) ?: 0
        viewModel.idMovie = movieId
        viewModel.getNumberOfImagesByType(MAP_OF_IMAGE_TYPE.keys)
        pagedAdapter =
            GalleryAdapter(resources.displayMetrics.widthPixels) { photoUrl -> onPhotoClick(photoUrl) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showActionBar()
        (activity as MainActivity).setActionBarTitle("Галерея")

        val myLayout = FlexboxLayoutManager(requireContext())
        myLayout.setFlexWrap(FlexWrap.WRAP)
        myLayout.flexDirection = FlexDirection.ROW
        myLayout.justifyContent = JustifyContent.CENTER
        myLayout.alignItems = AlignItems.CENTER

        binding.imagesRv.layoutManager = myLayout
        binding.imagesRv.adapter = pagedAdapter//myAdapter

        // собрать chipGroup
        viewModel.numberOfImagesByType.onEach {
            it.forEach { item ->
                val chip: Chip = View.inflate(
                    binding.galleryChipgroup.context,
                    R.layout.chip_templ,
                    null
                ) as Chip
                val label = "${MAP_OF_IMAGE_TYPE[item.key]} ${item.value}"

                val string = SpannableString(label)
                string.setSpan(
                    RelativeSizeSpan(0.7f),
                    string.lastIndexOf(" "),
                    string.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                chip.text = string

                chip.setOnClickListener {
                    lifecycleScope.launch {
                        viewModel.getImages(item.key).collectLatest { response ->
                                                pagedAdapter.submitData(response)
                        }
                    }
                }
                binding.galleryChipgroup.addView(chip)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isLoading.onEach {
            if (it) {
                binding.frameProgress.visibility = View.VISIBLE
            } else {
                binding.frameProgress.visibility = View.INVISIBLE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {// ожидание ошибки
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.take(1).collect { _ ->
                    BottomSheetErrorFragment().show(parentFragmentManager, "errordialog")
                }
            }
        }

        lifecycleScope.launch {// обработка ошибок пейджинга
            pagedAdapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh is LoadState.Error||loadStates.append is LoadState.Error) {
                    BottomSheetErrorFragment().show(parentFragmentManager, "errordialog")
                }
            }
        }
    }

    private fun onPhotoClick(photoUrl: String?) {
        val bundle = Bundle()
        listOfPhotoUrl.clear()
        pagedAdapter.snapshot().map { image -> //эти фото отправлены в слайдер
            listOfPhotoUrl.add(image?.imageUrl.toString())
        }

        if (photoUrl != null) {
            bundle.putCharSequence(PHOTO_URL, photoUrl)
            bundle.putStringArrayList(LIST_OF_PHOTO_URL, listOfPhotoUrl)
            findNavController().navigate(
                R.id.action_galleryFragment_to_sliderPhotoFragment,
                bundle
            )
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    companion object {
        const val PHOTO_URL = "ID_PHOTO"
        const val LIST_OF_PHOTO_URL = "LIST_OF_PHOTO_URL"

        val MAP_OF_IMAGE_TYPE = mapOf(
            Pair("STILL", "Кадры"),
            Pair("SHOOTING", "Изображения со съемок"),
            Pair("POSTER", "Постеры"),
            Pair("FAN_ART", "Фан-арты"),
            Pair("PROMO", "Промо"),
            Pair("CONCEPT", "Концепт-арты"),
            Pair("WALLPAPER", "Обои"),
            Pair("COVER", "Обложки"),
            Pair("SCREENSHOT", "Скриншоты"),
        )
    }
}