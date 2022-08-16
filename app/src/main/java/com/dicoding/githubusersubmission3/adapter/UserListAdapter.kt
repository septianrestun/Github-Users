package com.dicoding.githubusersubmission3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubusersubmission3.`interface`.ItemAdapterCallback
import com.dicoding.githubusersubmission3.R
import com.dicoding.githubusersubmission3.data.local.entity.UserEntity
import com.dicoding.githubusersubmission3.data.local.entity.UserFavLiveData
import com.dicoding.githubusersubmission3.databinding.UserItemBinding
import com.dicoding.githubusersubmission3.viewmodel.UserViewModel


class UserListAdapter(private val listUser: List<Any>, private val owner: LifecycleOwner, private val viewModel: UserViewModel): RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {

    private lateinit var itemCallback: ItemAdapterCallback<UserFavLiveData>

    class ListViewHolder(var binding: UserItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        when(val user =  listUser[position]){
            is UserFavLiveData -> {
                holder.binding.apply {
                    tvUsername.text = user.username

                    Glide.with(holder.itemView.context)
                        .load(user.avatarUrl)
                        .circleCrop()
                        .into(imgUser)
                    user.isFavLiveData.observe(owner){
                        if(it){
                            btnFav.setImageResource(R.drawable.ic_favred)
                        } else {
                            btnFav.setImageResource(R.drawable.ic_unfavred)
                        }
                        btnFav.setOnClickListener { view ->
                            if(it){
                                viewModel.removeFav(user)
                            } else {
                                viewModel.setFav(user)
                            }
                        }
                        holder.itemView.setOnClickListener { view ->
                            itemCallback.onItemClicked(
                                view,
                                user
                            )
                        }
                    }

                }
            }
            is UserEntity -> {
                holder.binding.apply {
                    tvUsername.text = user.username

                    Glide.with(holder.itemView.context)
                        .load(user.avatarUrl)
                        .circleCrop()
                        .into(imgUser)

                    val usersItem = UserFavLiveData(
                        user.username,
                        user.avatarUrl,
                        user.isFavorites,
                        viewModel.isFav(user.username)
                    )
                    if(user.isFavorites){
                        btnFav.setImageResource(R.drawable.ic_favred)
                    } else {
                        btnFav.setImageResource(R.drawable.ic_unfavred)
                    }
                    btnFav.setOnClickListener { view ->
                        if(user.isFavorites){
                            viewModel.removeFav(usersItem)
                        } else {
                            viewModel.setFav(usersItem)
                        }
                    }
                    holder.itemView.setOnClickListener { view -> itemCallback.onItemClicked(view, usersItem) }
                }
            }
        }
    }

    override fun getItemCount(): Int = listUser.size

    fun setAdapterItemCallback(callback: ItemAdapterCallback<UserFavLiveData>){
        this.itemCallback = callback
    }
}