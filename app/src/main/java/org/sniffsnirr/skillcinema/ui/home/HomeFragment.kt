package org.sniffsnirr.skillcinema.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphNavigator
import androidx.navigation.fragment.findNavController

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentHomeBinding
import org.sniffsnirr.skillcinema.restrepository.KinopoiskApi
import org.sniffsnirr.skillcinema.ui.home.adapter.MainAdapter
import org.sniffsnirr.skillcinema.ui.home.model.MainModel
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

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
                viewModel.MoviesCollectionsForHomePage.collect {
                    //binding.mainRv.adapter = MainAdapter(it)
                    binding.mainRv.adapter = MainAdapter(it,{collectionModel ->onCollectionClick(collectionModel)},{string2->onMovieClick(string2)})
                     //binding.textHome.text = it.size.toString()
                }
            }
        }
    }

    fun onCollectionClick(collectionModel:MainModel ){
        when(collectionModel.categoryDescription.first){
                KinopoiskApi.PREMIERES.first ->{
                    val bundle = Bundle()
                    val arraylist = ArrayList<MovieRVModel>()
                    arraylist.addAll(collectionModel.MovieRVModelList)
                    Log.d("collection in", "${collectionModel.category}")
                    //collectionModel.MovieRVModelList.toCollection(arraylist)
                    bundle.putParcelableArrayList(COLLECTION_MODEL, arraylist)
                    bundle.putCharSequence(COLLECTION_NAME,collectionModel.category)
                    findNavController().navigate(R.id.action_navigation_home_to_collectionFragment,bundle)
                }
                //KinopoiskApi.TOP_250_MOVIES=Pair<String,String>("TOP_250_MOVIES","Топ-250")
                //val POPULAR_SERIES=Pair<String,String>("POPULAR_SERIES","Популярные сериалы")
                //val PREMIERES=Pair<String,String>("PREMIERES","Премьеры")
                //val DYNAMIC=Pair<String,String>("DYNAMIC","Основаны на фильтах")


        else ->Log.d("ButtonClick", collectionModel.categoryDescription.first)}
    }

    fun onMovieClick(string :String){
        Log.d("ButtonClick", string)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val COLLECTION_MODEL="COLLECTION_MODEL"
        const val COLLECTION_NAME="COLLECTION_NAME"
    }
}