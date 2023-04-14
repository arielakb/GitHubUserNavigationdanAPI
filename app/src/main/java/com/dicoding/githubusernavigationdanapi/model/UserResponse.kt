package com.dicoding.githubusernavigationdanapi.model

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("items")
	val items : ArrayList<UserMain>
)
data class UserMain(

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("html_url")
	val htmlUrl: String
)
