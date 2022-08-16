package com.dicoding.githubusersubmission3.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusersubmission3.R
import com.dicoding.githubusersubmission3.`interface`.ItemAdapterCallback
import com.dicoding.githubusersubmission3.adapter.UserListAdapter
import com.dicoding.githubusersubmission3.data.local.entity.UserEntity
import com.dicoding.githubusersubmission3.data.local.entity.UserFavLiveData
import com.dicoding.githubusersubmission3.databinding.FragmentFavoritesBinding
import com.dicoding.githubusersubmission3.viewmodel.UserViewModel
import com.dicoding.githubusersubmission3.viewmodel.ViewModelFactory

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private var userList = ArrayList<UserEntity>()
    private lateinit var rvAdapter: UserListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Favorites"

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }
        rvAdapter = UserListAdapter(userList, viewLifecycleOwner, viewModel)

        binding.apply {
            rvFavoriteUser.setHasFixedSize(true)
            rvFavoriteUser.layoutManager = LinearLayoutManager(context)
            rvFavoriteUser.adapter = rvAdapter
        }

        rvAdapter.setAdapterItemCallback(object : ItemAdapterCallback<UserFavLiveData>{
            override fun onItemClicked(view: View?, data: UserFavLiveData) {
                view?.let {
                    val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailUserFragment(data.username)
                    view.findNavController().navigate(action)
                }?: run{
                    Toast.makeText(activity, "Gagal membuka detail ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.getFavUser().observe(viewLifecycleOwner){
            binding.pbFavoriteUser.visibility = View.GONE
            if(it.isEmpty()){
                binding.tvNoFav.visibility = View.VISIBLE
            }
            showList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.removeItem(R.id.menu_fav)
        menu.removeItem(R.id.menu_search)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showList(users: List<UserEntity>){
        userList.clear()
        userList.addAll(users)
        rvAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}