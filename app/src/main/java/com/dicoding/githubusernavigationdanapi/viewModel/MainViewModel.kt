package com.dicoding.githubusernavigationdanapi.viewModel

import  android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubusernavigationdanapi.BuildConfig
import com.dicoding.githubusernavigationdanapi.model.UserMain
import com.dicoding.githubusernavigationdanapi.model.UserResponse
import com.dicoding.githubusernavigationdanapi.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _userMain = MutableLiveData<ArrayList<UserMain>>()
    val userMain: LiveData<ArrayList<UserMain>> = _userMain

    init {
        findUser("\"\"")
    }

    fun findUser(query: String) {
        _isLoading.value = true

         val tokenAuth = BuildConfig.API_KEY

        RetrofitClient.getApiService().searchUsername(token = "Bearer $tokenAuth", query).apply {
            enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _userMain.value = response.body()?.items
                        }

                    } else Log.e(TAG, response.message())

                    _isLoading.value = false
                    _isError.value = false
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e(TAG, t.message.toString())

                    _userMain.value = arrayListOf()
                    _isError.value = true
                    _isLoading.value = false
                }

            })
        }
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

}
