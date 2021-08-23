package com.example.weather

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.weather.databinding.ViewPager2Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ViewPagerFragment : Fragment() {

    var listWeatherInDataFormat: List<Data> = defaultList
        private set
    private val viewModel: ViewModel by viewModels()

    var city: String? = null
        private set

    lateinit var binding: ViewPager2Binding
    private var dialog: Dialog? = null

    lateinit var cityNames: Array<String>
    lateinit var cityIds: Array<Int>

    val args: ViewPagerFragmentArgs by navArgs()

    companion object {

        const val CountPages = 4
        val defaultList = List(4) { Data(0, "NAN", "NAN", "NAN") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = savedInstanceState
        city = bundle?.getString("city") ?: args.city
        cityNames = resources.getStringArray(R.array.cities)
        cityIds = resources.getIntArray(R.array.cities_id).toTypedArray()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ViewPager2Binding.inflate(inflater, container, false)
        binding.lifecycleOwner = LifecycleOwner { lifecycle }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listWeatherInDataFormat = defaultList


        dialog = Dialog(requireContext()).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(ProgressBar(context).apply {
                visibility = ProgressBar.VISIBLE
                isClickable = false
            })
            setOnCancelListener { viewModel.clickCancelDialog(parentFragmentManager) }
        }
        dialog?.show()

        viewModel.setCityNamesAndIds(cityNames, cityIds)
        viewModel.setCity(city)

        val newListData = viewModel.listData.mapLatest {
            if (it != defaultList)
                dialog?.dismiss()
            it
        }.stateIn(
            GlobalScope,
            SharingStarted.Eagerly, defaultList
        )
        binding.viewPager2.adapter = ViewPagerAdapter(newListData, lifecycle)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("city", city)
    }
}

