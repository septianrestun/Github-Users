package com.dicoding.githubusersubmission3.data.remote.retrofit

import com.dicoding.githubusersubmission3.data.remote.response.UserItem
import com.dicoding.githubusersubmission3.data.remote.response.UserListResponse
import com.dicoding.githubusersubmission3.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun searchUsers(
        @Query("q") username: String
    ): Call<UserListResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUser(
        @Path("username") username: String
    ): Call<UserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<UserItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<UserItem>>
}