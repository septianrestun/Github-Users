package com.dicoding.githubusersubmission3.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.githubusersubmission3.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE isFavorites = 1")
    fun getFav(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFav(user: UserEntity)

    @Delete
    fun removeFav(user: UserEntity)

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username)")
    fun isFav(username: String): LiveData<Boolean>
}