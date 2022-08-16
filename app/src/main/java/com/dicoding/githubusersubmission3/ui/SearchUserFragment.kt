package com.dicoding.githubusersubmission3.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusersubmission3.`interface`.ItemAdapterCallback
import com.dicoding.githubusersubmission3.adapter.UserListAdapter
import com.dicoding.githubusersubmission3.data.local.entity.UserFavLiveData
import com.dicoding.githubusersubmission3.databinding.FragmentSearchUserBinding
import com.dicoding.githubusersubmission3.viewmodel.UserViewModel
import com.dicoding.githubusersubmission3.viewmodel.ViewModelFactory
import com.dicoding.githubusersubmission3.data.Result
import java.util.*

class SearchUserFragment : Fragment() {
    private lateinit var rvAdapter: UserListAdapter
    private var _binding: FragmentSearchUserBinding? = null
    private val binding get() = _binding!!
    private val userList = ArrayList<UserFavLiveData>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels{
            factory
        }

        rvAdapter = UserListAdapter(userList, viewLifecycleOwner, viewModel)

        binding.apply {
            rvSearchUserList.setHasFixedSize(true)
            rvSearchUserList.layoutManager = LinearLayoutManager(context)
            rvSearchUserList.adapter = rvAdapter
        }
        rvAdapter.setAdapterItemCallback(object : ItemAdapterCallback<UserFavLiveData> {
            override fun onItemClicked(view: View?, data: UserFavLiveData) {
                view?.let {
                    val action =  SearchUserFragmentDirections.actionFamousUserFragmentToDetailUserActivity(data.username)
                    view.findNavController().navigate(action)
                } ?: kotlin.run {
                    Toast.makeText(activity, "Gagal membuka detail ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.resetSearchUser()
        viewModel.searchRes.observe(viewLifecycleOwner){ result ->
            when (result){
                is Result.Loading -> {
                    binding.pbSearch.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbSearch.visibility = View.GONE
                    userList.clear()
                    userList.addAll(result.data)
                    rvAdapter.notifyDataSetChanged()
                }
                is Result.Error -> {
                    binding.pbSearch.visibility = View.GONE
                    Toast.makeText(context, "Terjadi Kesalahan " + result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvSearch.addTextChangedListener(
            object : TextWatcher{
                var timer = Timer()

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun afterTextChanged(text: Editable?) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(object : TimerTask(){
                        override fun run() {
                            activity?.runOnUiThread { viewModel.searchUser(text.toString()) }
                        }
                    }, 1500)
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}