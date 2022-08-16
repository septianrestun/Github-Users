package com.dicoding.githubusersubmission3.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.githubusersubmission3.data.local.preferences.SettingPreferences
import com.dicoding.githubusersubmission3.data.local.room.UserDatabase
import com.dicoding.githubusersubmission3.data.remote.retrofit.ApiConfig
import com.dicoding.githubusersubmission3.data.repository.UserRepository
import com.dicoding.githubusersubmission3.helper.AppExecutors


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {

    fun provideRepository(context: Context): UserRepository{
        val apiService = ApiConfig.getServiceApi()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        val pref = SettingPreferences.getInstance(context.dataStore)

        return UserRepository.getInstance(apiService, dao, appExecutors, pref)
    }

}