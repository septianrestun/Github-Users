package com.dicoding.githubusersubmission3.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubusersubmission3.ui.FollowersFragment
import com.dicoding.githubusersubmission3.ui.FollowingFragment

class ConnectionPageAdapter(activity: AppCompatActivity, private val username: String): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment
        when(position){
            0 -> {
                fragment = FollowingFragment()
                fragment.arguments = Bundle().apply {
                    putString("username", username)
                }
            }
            else -> {
                fragment = FollowersFragment()
                fragment.arguments = Bundle().apply {
                    putString("username", username)
                }
            }
        }
        return fragment
    }
}