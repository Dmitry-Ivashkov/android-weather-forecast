package com.example.weather.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.viewModels.ViewModel
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

    private val viewModel: ViewModel by viewModels()


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
                if (viewModel.isConected()) {
                    findNavController().navigate(
                        ListCityFragmentDirections.actionListCityFragmentToPagesWeatherFragment(item)
                    )
                } else {
                    object : AlertDialog(context) {}
                        .run {
                            setMessage("нет доступа к интернету")
                            show()
                        }
                }
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                ContextCompat.getDrawable(context, R.drawable.item_divider)?.let { setDrawable(it) }
            })
        }
    }
}