package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AstroidItemLayoutBinding

class AsteroidsAdapter(private val clickListener: AsteroidListener) :
    ListAdapter<Asteroid, AsteroidViewHolder>(DiffCallback) {


    class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val binding = AstroidItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AsteroidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.also {
            it.bind(asteroid)
            it.itemView.setOnClickListener {
                clickListener.onClick(asteroid)
            }

        }
    }


}

class AsteroidViewHolder(private val binding: AstroidItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(asteroid: Asteroid) {
        binding.asteroid = asteroid
        binding.executePendingBindings()
    }

}
