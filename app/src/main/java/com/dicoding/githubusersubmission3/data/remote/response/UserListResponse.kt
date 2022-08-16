package com.dicoding.githubusersubmission3.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserListResponse(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @field:SerializedName("items")
    val users: List<UserItem>
)
