package com.dicoding.githubusersubmission3.`interface`

import android.view.View

interface ItemAdapterCallback<T> {
    fun onItemClicked(view: View?, data: T)
}