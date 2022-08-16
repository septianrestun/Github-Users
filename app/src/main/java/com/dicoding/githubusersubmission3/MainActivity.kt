package com.dicoding.githubusersubmission3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.dicoding.githubusersubmission3.databinding.ActivityMainBinding
import com.dicoding.githubusersubmission3.ui.FamousUserFragmentDirections
import com.dicoding.githubusersubmission3.viewmodel.UserViewModel
import com.dicoding.githubusersubmission3.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory : ViewModelFactory = ViewModelFactory.getInstance(this@MainActivity)
        val vm: UserViewModel by viewModels {
            factory
        }
        this.viewModel = vm
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        viewModel.getThemeSettings().observe(this){
            val mode: Int
            if(it){
                mode = AppCompatDelegate.MODE_NIGHT_YES
                menu?.findItem(R.id.menu_mode)?.setIcon(R.drawable.ic_night)
            } else {
                mode = AppCompatDelegate.MODE_NIGHT_NO
                menu?.findItem(R.id.menu_mode)?.setIcon(R.drawable.ic_day)
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_search -> {
                val action = FamousUserFragmentDirections.actionFamousUserFragmentToSearchUserFragment()
                binding.fragmentContainerView.findNavController().navigate(action)
            }
            R.id.menu_mode -> {
                val mode : Int
                if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                    viewModel.setThemeSettings(false)
                } else {
                    viewModel.setThemeSettings(true)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}