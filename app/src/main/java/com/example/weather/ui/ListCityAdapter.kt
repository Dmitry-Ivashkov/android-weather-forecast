package com.example.weather.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ListCityItemBinding

class ListCityAdapter(
    private val values: List<String>,
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<ListCityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListCityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = item
        holder.contentView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(binding: ListCityItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.city
    }

}