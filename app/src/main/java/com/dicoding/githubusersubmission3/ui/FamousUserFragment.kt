package com.dicoding.githubusersubmission3.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusersubmission3.R
import com.dicoding.githubusersubmission3.`interface`.ItemAdapterCallback
import com.dicoding.githubusersubmission3.adapter.UserListAdapter
import com.dicoding.githubusersubmission3.data.local.entity.UserFavLiveData
import com.dicoding.githubusersubmission3.databinding.FragmentFamousUserBinding
import com.dicoding.githubusersubmission3.viewmodel.UserViewModel
import com.dicoding.githubusersubmission3.viewmodel.ViewModelFactory
import com.dicoding.githubusersubmission3.data.Result


class FamousUserFragment : Fragment() {
    private var _binding: FragmentFamousUserBinding? = null
    private val binding get() = _binding!!
    private val userList = ArrayList<UserFavLiveData>()
    private lateinit var adapter: UserListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamousUserBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }
        adapter = UserListAdapter(userList, viewLifecycleOwner, viewModel)

        binding.apply {
            rvFamousUserList.setHasFixedSize(true)
            rvFamousUserList.layoutManager = LinearLayoutManager(context)
            rvFamousUserList.adapter = adapter
        }

        adapter.setAdapterItemCallback(object : ItemAdapterCallback<UserFavLiveData>{
            override fun onItemClicked(view: View?, data: UserFavLiveData) {
                view?.let {
                    val action = FamousUserFragmentDirections.actionFamousUserFragmentToDetailUserActivity(data.username)
                    view.findNavController().navigate(action)
                }?: run{
                    Toast.makeText(activity, "Gagal menampilkan detal ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.getFamousUser().observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Loading -> {
                    binding.pbFamousUserList.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbFamousUserList.visibility = View.GONE
                    showList(result.data)
                }
                is Result.Error -> {
                    binding.pbFamousUserList.visibility = View.GONE
                    Toast.makeText(context, "Terjadi Kesalahan" + result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_fav -> {
                val action = FamousUserFragmentDirections.actionFamousUserFragmentToFavoritesFragment()
                activity?.findNavController(R.id.fragmentContainerView)?.navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Github User"
            show()
        }
    }

    private fun showList(users: List<UserFavLiveData>){
        userList.clear()
        userList.addAll(users)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}