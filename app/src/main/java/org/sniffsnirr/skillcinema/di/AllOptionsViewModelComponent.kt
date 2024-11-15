package org.sniffsnirr.skillcinema.di

import dagger.Component
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.ui.search.options.AllOptionsViewModel
import javax.inject.Singleton


@Component
@Singleton
@ActivityRetainedScoped
interface AllOptionsViewModelComponent {
    fun inject(allOptionsViewModel: AllOptionsViewModel)
}