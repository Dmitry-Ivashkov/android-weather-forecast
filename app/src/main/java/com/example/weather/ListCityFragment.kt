package com.example.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.ListCityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class ListCityFragment : Fragment() {

    lateinit var binding: ListCityBinding
    private val recyclerView
        get() = binding.list


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ListCityBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(this.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = ListCityAdapter(
                resources.getStringArray(R.array.cities).toList()
            ) { item ->
                findNavController().navigate(
                    ListCityFragmentDirections.actionListCityFragmentToPagesWeatherFragment(item)
                )
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                ContextCompat.getDrawable(context, R.drawable.item_divider)?.let { setDrawable(it) }
            })
        }
    }
}