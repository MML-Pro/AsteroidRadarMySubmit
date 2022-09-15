package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.util.FilterAsteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MainViewModel"
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _navigateToDetailAsteroid = MutableLiveData<Asteroid?>()
    val navigateToDetailAsteroid: LiveData<Asteroid?>
        get() = _navigateToDetailAsteroid

    private var _filterAsteroid = MutableLiveData(FilterAsteroid.ALL)


    val asteroidList = Transformations.switchMap(_filterAsteroid) {
        when (it!!) {
            FilterAsteroid.WEEK -> asteroidRepository.weekAsteroids
            FilterAsteroid.TODAY -> asteroidRepository.todayAsteroids
            else -> asteroidRepository.allAsteroids
        }
    }

    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            refreshPictureOfDay()
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToDetailAsteroid.value = null
    }

    fun onChangeFilter(filter: FilterAsteroid) {
        _filterAsteroid.postValue(filter)
    }


    private suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                _pictureOfDay.postValue(
                    NasaApiService.AsteroidApi.retrofitService.getPictureOfTheDay(Constants.API_KEY)
                )
            } catch (err: Exception) {
                Log.e("refreshPictureOfDay", err.printStackTrace().toString())
            }
        }
    }
}