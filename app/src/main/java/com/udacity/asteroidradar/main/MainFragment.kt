package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

private const val TAG = "MainFragment"

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var adapter: AsteroidsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)

        adapter = AsteroidsAdapter()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.asteroidRecycler.adapter = adapter

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAsteroids()

        viewModel.response.observe(viewLifecycleOwner){
//            Log.d(TAG, "onViewCreated: ${it[0].toString()}")
            it?.let {
                adapter.submitList(it)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java", ReplaceWith("true"))
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
