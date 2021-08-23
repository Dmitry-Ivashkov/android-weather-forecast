package com.example.weather

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ViewPagerItemBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*

class ViewPagerAdapter(
    private val array: StateFlow<List<Data>>,
    val lifecycle: Lifecycle
) : RecyclerView.Adapter<ViewPager2ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPager2ViewHolder {


        return ViewPager2ViewHolder(
            ViewPagerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPager2ViewHolder, position: Int) {
        holder.binding.lifecycleOwner = LifecycleOwner { lifecycle }

        holder.binding.data = array
            .mapLatest { list -> list[position] }
            .stateIn(
                GlobalScope,
                SharingStarted.Eagerly, array.value[position]
            )
    }

    override fun getItemCount(): Int = ViewPagerFragment.CountPages
}

class ViewPager2ViewHolder(val binding: ViewPagerItemBinding) :
    RecyclerView.ViewHolder(binding.root)