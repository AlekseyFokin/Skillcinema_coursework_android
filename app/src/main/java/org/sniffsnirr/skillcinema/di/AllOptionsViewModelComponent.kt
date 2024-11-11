package org.sniffsnirr.skillcinema.di

import dagger.Component
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel
import org.sniffsnirr.skillcinema.ui.search.options.AllOptionsViewModel
import javax.inject.Singleton


@Component
@Singleton
@ActivityRetainedScoped

//@InstallIn(SingletonComponent::class)

interface AllOptionsViewModelComponent {

    // Factory to create instances of the AppComponent

 //   @Provides
  //  fun provideGetCountriesAndGenresUsecase(kinopoiskRepository: KinopoiskRepository): GetCountriesAndGenresUsecase
    fun inject(allOptionsViewModel: AllOptionsViewModel)
    //{return GetCountriesAndGenresUsecase(kinopoiskRepository)}

//    @Provides
//    fun provideKinopoiskRepository(kinopoiskDataSource: KinopoiskDataSource): KinopoiskRepository
//    {return KinopoiskRepository(kinopoiskDataSource)}




}