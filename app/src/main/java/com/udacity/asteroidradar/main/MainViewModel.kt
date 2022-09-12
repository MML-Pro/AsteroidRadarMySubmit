package com.udacity.asteroidradar.main

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainViewModel"
class MainViewModel : ViewModel() {

    private val _response = MutableLiveData<List<Asteroid>>()
    val response:LiveData<List<Asteroid>> get() = _response


    fun getAsteroids() {

            NasaApiService.AsteroidApi.retrofitService.getAsteroids(Constants.API_KEY).enqueue(
                object : Callback<String>{
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful){
                            response.body()?.let {

                                val obj = JSONObject(it)

                                Log.d(TAG, "onResponse: JSONObject ${obj.toString()}")

                                _response.value = parseAsteroidsJsonResult(obj)

                                Log.d(TAG, "onResponse: ${_response.value.toString()}")

                                Log.d(TAG, "onResponse: $it")
                            }
                        }else {
                            Log.e(TAG, "onResponse: ${response.errorBody().toString()}" )
                            Log.e(TAG, "onResponse: ${response.message()}" )
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message.toString()}" )
                        Log.e(TAG, "onFailure: ${t.cause.toString()}" )
                    }

                }
            )

        }
    }