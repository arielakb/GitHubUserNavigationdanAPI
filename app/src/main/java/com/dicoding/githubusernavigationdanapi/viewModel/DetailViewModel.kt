package com.dicoding.githubusernavigationdanapi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubusernavigationdanapi.BuildConfig
import com.dicoding.githubusernavigationdanapi.model.DetailResponse
import com.dicoding.githubusernavigationdanapi.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel(){

    private val _callCounter = MutableLiveData(0)
    val callCounter: LiveData<Int> = _callCounter

    private val _userDetail = MutableLiveData<DetailResponse?>(null)
    val userDetail: LiveData<DetailResponse?> = _userDetail


    fun getUserDetail(username: String) {
        _callCounter.value = 1

         val tokenAuth = BuildConfig.API_KEY

        RetrofitClient.getApiService().getUserDetail(token = "Bearer $tokenAuth", username).apply {
            enqueue(object : Callback<DetailResponse> {
                override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _userDetail.value = response.body()
                        }

                    } else Log.e(TAG, response.message())

                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            })
        }
    }

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }
}