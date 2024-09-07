package org.sniffsnirr.skillcinema.ui.onemovie.gallery

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentGalleryBinding
import org.sniffsnirr.skillcinema.ui.collections.paging.PagingLoadStateAdapter
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val viewModel: GalleryViewModel by viewModels()
    private var _binding: FragmentGalleryBinding? = null
    val binding get() = _binding!!
    private var movieId = 0
    val listOfPhotoUrl = ArrayList<String>()
    lateinit var pagedAdapter:GalleryAdapter
    //  private val pagedAdapter = GalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = arguments?.getInt(ID_MOVIE) ?: 0
        (activity as MainActivity).showActionBar()
        (activity as MainActivity).setActionBarTitle("Галерея")
        Log.d("ID_MoVIE", "$movieId")
        viewModel.idMovie = movieId
        viewModel.getNumberOfImagesByType(MAP_OF_IMAGE_TYPE.keys)

      //  val displayMetrics = requireContext().resources.displayMetrics
      //  val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        pagedAdapter= GalleryAdapter(resources.displayMetrics.widthPixels) { photoUrl -> onPhotoClick(photoUrl) }
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

        val myLayout = FlexboxLayoutManager(requireContext())
        myLayout.setFlexWrap(FlexWrap.WRAP)
        myLayout.setFlexDirection(FlexDirection.ROW)
        myLayout.setJustifyContent(JustifyContent.CENTER)
        myLayout.setAlignItems(AlignItems.CENTER)


        val footerAdapter = PagingLoadStateAdapter()
        val myAdapter = pagedAdapter.withLoadStateHeader(footerAdapter)

        binding.imagesRv.layoutManager = myLayout
        binding.imagesRv.adapter = myAdapter

        // собрать chipGroup
        viewModel.numberOfImagesByType.onEach {
            it.forEach { item ->
                Log.d("MAP_GALLERY", "${item.key} - ${item.value}")
                val chip: Chip = View.inflate(
                    binding.galleryChipgroup.context,
                    R.layout.chip_templ,
                    null
                ) as Chip
                var label = "${MAP_OF_IMAGE_TYPE.get(item.key)} ${item.value}"

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
                Log.d("progressbar", "on")
            } else {
                binding.frameProgress.visibility = View.INVISIBLE
                Log.d("progressbar", "off")
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onPhotoClick(photoUrl: String?) {
        Log.d("onPhotoClick", "")
        val bundle = Bundle()
        listOfPhotoUrl.clear()
        pagedAdapter.snapshot().map { image ->
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
        val PHOTO_URL = "ID_PHOTO"
        val LIST_OF_PHOTO_URL = "LIST_OF_PHOTO_URL"

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