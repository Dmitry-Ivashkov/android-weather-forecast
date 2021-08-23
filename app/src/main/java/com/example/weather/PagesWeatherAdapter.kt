package com.example.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.PagesWeatherItemBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*

class PagesWeatherAdapter(
    private val array: StateFlow<List<Data>>,
    val lifecycle: Lifecycle
) : RecyclerView.Adapter<PagesWeatherViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagesWeatherViewHolder =
        PagesWeatherViewHolder(
            PagesWeatherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PagesWeatherViewHolder, position: Int) {
        holder.binding.lifecycleOwner = LifecycleOwner { lifecycle }

        holder.binding.data = array
            .mapLatest { list -> list[position] }
            .stateIn(
                GlobalScope,
                SharingStarted.Eagerly,
                array.value[position]
            )
    }

    override fun getItemCount(): Int = PagesWeatherFragment.CountPages
}

class PagesWeatherViewHolder(val binding: PagesWeatherItemBinding) :
    RecyclerView.ViewHolder(binding.root)