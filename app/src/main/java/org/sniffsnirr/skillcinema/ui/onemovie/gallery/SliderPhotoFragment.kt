package org.sniffsnirr.skillcinema.ui.onemovie.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.request.RequestOptions
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.animations.DescriptionAnimation
import com.glide.slider.library.slidertypes.BaseSliderView
import com.glide.slider.library.slidertypes.DefaultSliderView
import com.glide.slider.library.tricks.ViewPagerEx
import dagger.hilt.android.AndroidEntryPoint
import org.sniffsnirr.skillcinema.databinding.FragmentSliderPhotoBinding


@AndroidEntryPoint
class SliderPhotoFragment : Fragment(), BaseSliderView.OnSliderClickListener,
    ViewPagerEx.OnPageChangeListener {

    private val viewModel: SliderPhotoViewModel by viewModels()
    private var _binding: FragmentSliderPhotoBinding? = null
    val binding get() = _binding!!

    private lateinit var slider: SliderLayout
    private var photoUrl = ""
    private lateinit var listOfUrl: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoUrl = arguments?.getCharSequence(GalleryFragment.PHOTO_URL).toString()
        listOfUrl = arguments?.getStringArrayList(GalleryFragment.LIST_OF_PHOTO_URL) ?: ArrayList()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSliderPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slider = binding.slider
        val requestOptions = RequestOptions()
        requestOptions.centerCrop()

        listOfUrl.map { url ->
            val sliderView = DefaultSliderView(slider.context)
            Log.d("slider", "$url")
            sliderView
                .image(url)
                .description(url)
                .setRequestOption(requestOptions)
                .setProgressBarVisible(true)
                .setOnSliderClickListener(this);
            slider.addSlider(sliderView);
        }

        slider.setPresetTransformer(SliderLayout.Transformer.Accordion)

        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        slider.setCustomAnimation(DescriptionAnimation())
        slider.setDuration(400)
        slider.addOnPageChangeListener(this)
        slider.stopCyclingWhenTouch(false)
//slider.setCurrentPosition(listOfUrl.lastIndexOf(photoUrl),false)
    }

    override fun onSliderClick(slider: BaseSliderView?) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onStop() {
        super.onStop()
        slider.stopAutoCycle();
    }
}