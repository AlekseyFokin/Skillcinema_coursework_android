package org.sniffsnirr.skillcinema.di

import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel
import org.sniffsnirr.skillcinema.usecases.GetCountriesAndGenresUsecase
import javax.inject.Singleton

@Component
@ActivityRetainedScoped
@Singleton
//@InstallIn(SingletonComponent::class)
interface SearchViewModelComponent {

 //   @Provides
  //  fun provideGetCountriesAndGenresUsecase(kinopoiskRepository: KinopoiskRepository): GetCountriesAndGenresUsecase
    fun inject(searchViewModel: SearchViewModel)
    //{return GetCountriesAndGenresUsecase(kinopoiskRepository)}

//    @Provides
//    fun provideKinopoiskRepository(kinopoiskDataSource: KinopoiskDataSource): KinopoiskRepository
//    {return KinopoiskRepository(kinopoiskDataSource)}


}