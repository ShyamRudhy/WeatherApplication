package com.shyam.weatherapp.data.repository.launcher
import com.shyam.weatherapp.data.api.ApiService
import com.shyam.weatherapp.data.api.BaseRemoteDataSource
import com.shyam.weatherapp.ui.launcher.LauncherViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LauncherRepository @Inject constructor(private val service: ApiService) : BaseRemoteDataSource() {

    suspend fun checkNavigationFlow() : LauncherViewModel.NavigationState{
        return LauncherViewModel.NavigationState.HOME_PAGE
    }

}