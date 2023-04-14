package com.dicoding.githubusernavigationdanapi.network

import com.dicoding.githubusernavigationdanapi.model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @GET("search/users")
    fun searchUsername(
        @Header("Authorization") token: String,
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<ArrayList<UserMain>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<ArrayList<UserMain>>
}