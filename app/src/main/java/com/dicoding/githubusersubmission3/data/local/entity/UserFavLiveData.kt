package com.dicoding.githubusersubmission3.data.local.entity

import androidx.lifecycle.LiveData

class UserFavLiveData(username: String, avatar: String, isFav: Boolean, val isFavLiveData: LiveData<Boolean>): UserEntity(username, avatar, isFav)