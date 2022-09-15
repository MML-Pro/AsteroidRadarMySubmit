package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.util.FilterAsteroid

private const val TAG = "MainFragment"

class MainFragment : Fragment() , MenuProvider{

    private lateinit var menuHost: MenuHost

    private val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(app = requireActivity().application)
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[MainViewModel::class.java]
    }

    private val asteroidAdapter = AsteroidsAdapter(AsteroidsAdapter.AsteroidListener { asteroid ->
        viewModel.onAsteroidClicked(asteroid)
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        menuHost = requireActivity()

        menuHost.addMenuProvider(this,viewLifecycleOwner,Lifecycle.State.CREATED)

        binding.viewModel = viewModel
        binding.asteroidRecycler.adapter = asteroidAdapter

        viewModel.navigateToDetailAsteroid.observe(viewLifecycleOwner) { asteroid ->
            if (asteroid != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onAsteroidNavigated()
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        menuHost.removeMenuProvider(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asteroidList.observe(viewLifecycleOwner) { asteroid ->
            asteroid.apply {
                asteroidAdapter.submitList(this)
            }
        }
    }



    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        viewModel.onChangeFilter(
            when (menuItem.itemId) {
                R.id.show_rent_menu -> {
                    FilterAsteroid.TODAY
                }
                R.id.show_all_menu -> {
                    FilterAsteroid.WEEK
                }
                else -> {
                    FilterAsteroid.ALL
                }
            }
        )
        return true
    }
}
