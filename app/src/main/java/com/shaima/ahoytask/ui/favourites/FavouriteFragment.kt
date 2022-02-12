package com.shaima.ahoytask.ui.favourites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.shaima.ahoytask.R
import com.shaima.ahoytask.databinding.FragmentDashboardBinding
import com.shaima.ahoytask.domain.settings.usecase.GetSettingsUseCase
import com.shaima.ahoytask.domain.state.HomeFavFragmentState
import com.shaima.ahoytask.ui.favourites.adapters.FavAdapter
import com.shaima.ahoytask.ui.home.adapters.HomeWeatherAdapter
import com.shaima.ahoytask.utils.ext.gone
import com.shaima.ahoytask.utils.ext.showToast
import com.shaima.ahoytask.utils.ext.visible
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private lateinit var favouriteViewModel: FavouriteViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var useCase: GetSettingsUseCase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         favouriteViewModel =
            ViewModelProvider(this).get(FavouriteViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        listiners()
        setupRecyclerView()
        return root
    }

    private fun listiners() {
        favouriteViewModel.viewState.observe(viewLifecycleOwner) {
            when (it) {
                is HomeFavFragmentState.SuccessGetFromDb -> handelWeather(it.weatherEntity)
                is HomeFavFragmentState.IsLoading -> handleLoading(it.isLoading)
                is HomeFavFragmentState.ShowToast -> requireActivity().showToast(it.message)
                is HomeFavFragmentState.Init -> Unit
            }
        }

    }

    private fun setupRecyclerView() {
        val mAdapter = FavAdapter(mutableListOf() )
        mAdapter.setItemTapListener(object : FavAdapter.OnItemTap{
            override fun onTap(weather: WeatherEntity) {
                val bundle = bundleOf("weather" to weather)
                findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_home3, bundle)
            }
        })
        binding.weatherRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingProgressBar.visible()
        } else {
            binding.loadingProgressBar.gone()
        }
    }

    private fun handelWeather(weatherEntity: List<WeatherEntity>) {
        binding.weatherRecyclerView.adapter?.let {
            if (it is FavAdapter) {
                Log.d("inFavList", Gson().toJson(weatherEntity))
                it.updateList(useCase.invoke(), weatherEntity)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}