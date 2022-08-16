package com.dicoding.githubusersubmission3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubusersubmission3.data.local.entity.UserFavLiveData
import com.dicoding.githubusersubmission3.data.repository.UserRepository

class UserViewModel(private val userRepo: UserRepository): ViewModel() {

    val searchRes = userRepo.searchResult

    fun getFamousUser() = userRepo.getFamousUser()

    fun getFavUser() = userRepo.getFavUsers()

    fun setFav(user: UserFavLiveData) = userRepo.setFavUser(user)

    fun removeFav(user: UserFavLiveData) = userRepo.removeFavUser(user)

    fun searchUser(keyword: String) = userRepo.searchUser(keyword)

    fun resetSearchUser() = userRepo.resetSearch()

    fun getDetailUser(username: String) = userRepo.getDetailUser(username)

    fun getFollowers(username: String) = userRepo.getUserFollowers(username)

    fun getFollowing(username: String) = userRepo.getUserFollowing(username)

    fun isFav(username: String) = userRepo.isFavorites(username)

    fun getThemeSettings() = userRepo.getThemeSetting()

    fun setThemeSettings(status: Boolean) = userRepo.saveThemeSetting(status, viewModelScope)


}