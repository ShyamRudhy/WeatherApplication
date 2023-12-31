package com.shyam.weatherapp.ui.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shyam.weatherapp.base.PrivateSharedPrefManager
import com.shyam.weatherapp.data.repository.launcher.LauncherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(private val launcherRepository: LauncherRepository) : ViewModel() {

    @Inject
    lateinit var privateSharedPreferencesManager: PrivateSharedPrefManager

    private val _navigationListener = MutableLiveData<NavigationState>()
    val navigationListener: LiveData<NavigationState> = _navigationListener


    enum class NavigationState {
        ON_BOARDING_PAGE,
        HOME_PAGE,
        IDLE
    }

    init {
        _navigationListener.postValue(NavigationState.IDLE)
    }

    suspend fun getNavigateNextFlow(){
        _navigationListener.postValue(launcherRepository.checkNavigationFlow())
    }
}