package com.dicoding.githubusersubmission3.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusersubmission3.`interface`.ItemAdapterCallback
import com.dicoding.githubusersubmission3.adapter.UserListAdapter
import com.dicoding.githubusersubmission3.data.Result
import com.dicoding.githubusersubmission3.data.local.entity.UserFavLiveData
import com.dicoding.githubusersubmission3.databinding.FragmentConnectionBinding
import com.dicoding.githubusersubmission3.viewmodel.UserViewModel
import com.dicoding.githubusersubmission3.viewmodel.ViewModelFactory

class FollowersFragment : Fragment() {
    private var _binding: FragmentConnectionBinding? = null
    private val binding get() = _binding!!
    private val userList = ArrayList<UserFavLiveData>()
    private lateinit var rvAdapter: UserListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }

        rvAdapter = UserListAdapter(userList, viewLifecycleOwner, viewModel)

        binding.apply {
            rvUserConnection.setHasFixedSize(true)
            rvUserConnection.layoutManager = LinearLayoutManager(context)
            rvUserConnection.adapter = rvAdapter
        }

        rvAdapter.setAdapterItemCallback(object: ItemAdapterCallback<UserFavLiveData>{
            override fun onItemClicked(view: View?, data: UserFavLiveData) {
                view?.let {
                    val action = FamousUserFragmentDirections.actionFamousUserFragmentToDetailUserFragment(data.username)
                    view.findNavController().navigate(action)
                }?: run{
                    Toast.makeText(activity, "Gagal membuka detail ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        val username = arguments?.getString("username") ?: ""
        viewModel.getFollowers(username).observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Loading -> {
                    binding.pbUserConnection.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbUserConnection.visibility = View.GONE
                    showList(result.data)
                }
                is Result.Error -> {
                    binding.pbUserConnection.visibility = View.GONE
                    Toast.makeText(context, "Terjadi Kesalahan" + result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showList(users: List<UserFavLiveData>) {
        userList.clear()
        userList.addAll(users)
        rvAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}