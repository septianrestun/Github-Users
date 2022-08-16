package com.dicoding.githubusersubmission3.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dicoding.githubusersubmission3.R
import com.dicoding.githubusersubmission3.adapter.ConnectionPageAdapter
import com.dicoding.githubusersubmission3.data.Result
import com.dicoding.githubusersubmission3.data.local.entity.UserFavLiveData
import com.dicoding.githubusersubmission3.databinding.FragmentDetailUserBinding
import com.dicoding.githubusersubmission3.viewmodel.UserViewModel
import com.dicoding.githubusersubmission3.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator



class DetailUserFragment : Fragment() {
    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding!!
    private val args: DetailUserFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = args.username
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.removeItem(R.id.menu_search)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_fav -> {
                val action = DetailUserFragmentDirections.actionDetailUserFragmentToFavoritesFragment()
                activity?.findNavController(R.id.fragmentContainerView)?.navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        val pager = ConnectionPageAdapter(activity, args.username)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }
        binding.vpConnectionList.adapter = pager
        binding.vpConnectionList.visibility = View.INVISIBLE
        binding.tlConnection.visibility = View.INVISIBLE

        val username = args.username
        val favStatus = viewModel.isFav(username)

        viewModel.getDetailUser(username).observe(viewLifecycleOwner){
            result ->
            when(result){
                is Result.Loading -> {
                    binding.pbDetUser.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val data = result.data
                    binding.pbDetUser.visibility = View.GONE
                    binding.tlConnection.visibility = View.VISIBLE
                    binding.vpConnectionList.visibility = View.VISIBLE

                    binding.apply {
                        Glide.with(activity)
                            .load(data.avatarUrl)
                            .circleCrop()
                            .into(imgDetUser)
                        tvDetName.text = data.name
                        tvDetUsername.text = "@" + data.username
                        tvDetCompany.text = data.company
                        tvDetLocation.text = data.location

                        tvDetFollowing.text = data.following.toString()
                        tvDetFollowers.text = data.followers.toString()
                        tvDetRepository.text = data.publicRepos.toString()

                        TabLayoutMediator(tlConnection, vpConnectionList){
                            tab, position ->
                            tab.text = resources.getString(TAB_TITLES[position])
                        }.attach()
                    }
                    favStatus.observe(viewLifecycleOwner){
                        if(it){
                            binding.fabDetUser.setImageResource(R.drawable.ic_favred)
                        } else {
                            binding.fabDetUser.setImageResource(R.drawable.ic_unfavred)
                        }

                        val users = UserFavLiveData(
                            data.username,
                            data.avatarUrl,
                            it,
                            favStatus
                        )

                        binding.fabDetUser.setOnClickListener { _ ->
                            if(it){
                                viewModel.removeFav(users)
                            } else {
                                viewModel.setFav(users)
                            }
                        }
                    }
                }

                is Result.Error -> {
                    binding.pbDetUser.visibility = View.GONE
                    Toast.makeText(context, "Terjadi Kesalahan " + result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        private val TAB_TITLES = arrayListOf(
            R.string.tabFollowing,
            R.string.tabFollowers
        )
    }

}