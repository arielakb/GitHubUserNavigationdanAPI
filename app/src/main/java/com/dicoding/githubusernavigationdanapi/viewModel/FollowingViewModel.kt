package com.dicoding.githubusernavigationdanapi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubusernavigationdanapi.BuildConfig
import com.dicoding.githubusernavigationdanapi.model.UserMain
import com.dicoding.githubusernavigationdanapi.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _following = MutableLiveData<ArrayList<UserMain>?>(null)
    val following: LiveData<ArrayList<UserMain>?> = _following

    fun getUserFollowing(username: String) {
        _isLoading.value = true

        val tokenAuth = BuildConfig.API_KEY

        RetrofitClient.getApiService().getUserFollowing(token = "Bearer $tokenAuth", username)
            .apply {
                enqueue(object :  Callback<ArrayList<UserMain>> {
                    override fun onResponse(
                        call: Call<ArrayList<UserMain>>, response: Response<ArrayList<UserMain>>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                _following.value = response.body()
                            } else Log.e(TAG, response.message())
                            _isLoading.value = false
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<UserMain>>, t: Throwable) {
                        Log.e(TAG, t.message.toString())

                        _following.value = arrayListOf()
                        _isLoading.value = false
                    }

                })
            }
    }

    companion object {
        private val TAG = FollowingViewModel::class.java.simpleName
    }
}
