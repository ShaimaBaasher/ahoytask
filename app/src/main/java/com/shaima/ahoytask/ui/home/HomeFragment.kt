package com.shaima.ahoytask.ui.home

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.shaima.ahoytask.R
import com.shaima.ahoytask.databinding.FragmentHomeBinding
import com.shaima.ahoytask.domain.settings.usecase.GetSettingsUseCase
import com.shaima.ahoytask.domain.state.HomeFavFragmentState
import com.shaima.ahoytask.ui.home.adapters.HomeWeatherAdapter
import com.shaima.ahoytask.utils.ext.gone
import com.shaima.ahoytask.utils.ext.showToast
import com.shaima.ahoytask.utils.ext.visible
import com.shaima.api.repository.Utils
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var bundle: WeatherEntity? = null
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    @Inject
    lateinit var useCase: GetSettingsUseCase

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        textView = binding.textCity

//        NotifyWork.alarm(requireContext())
        checkLocationPermission()
        setupRecyclerView()
        getParams()
        setupSearch()
        return root
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.getSettings()
    }

    private fun getParams() {
        bundle = arguments?.getParcelable("weather")

        if (bundle == null) {
            observeState(homeViewModel)
        } else {
            handelWeather(bundle!!)
        }
    }

    private fun getFromDb() {
        homeViewModel.viewState.observe(viewLifecycleOwner) {
            when (it) {
                is HomeFavFragmentState.SuccessGetFromDb -> handelWeather(it.weatherEntity[0])
                is HomeFavFragmentState.IsLoading -> handleLoading(it.isLoading)
                is HomeFavFragmentState.ShowToast -> requireActivity().showToast(it.message)
                is HomeFavFragmentState.Init -> Unit
            }
        }
    }

    private fun listiners(weatherEntity: WeatherEntity) {
        binding.favIcon.setOnClickListener {
            //todo check if already exist
            Log.d("weatherEntity", Gson().toJson(weatherEntity))
            homeViewModel.storePlace(weatherEntity)
        }
    }

    private fun setupSearch() {
        binding.edtSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (Utils.checkValid(binding.edtSearch.text.toString().trim())) {
                    binding.edtSearch.clearFocus()
                    Utils.hideKeyboardFrom(requireContext(), binding.root)
                    homeViewModel.getWeatherByName(
                        binding.edtSearch.text.toString().trim().toLowerCase()
                    )
                } else Toast.makeText(
                    requireContext(),
                    "Please Enter Valid Input",
                    Toast.LENGTH_LONG
                ).show()
                binding.edtSearch.setText("")
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun setupRecyclerView() {
        val mAdapter = HomeWeatherAdapter(mutableListOf())
        binding.weatherRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun observeState(viewModel: HomeViewModel) {
        viewModel.mStateFav
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: HomeFavFragmentState) {
        when (state) {
            is HomeFavFragmentState.SuccessGetWeather -> handelWeather(state.weatherEntity)
            is HomeFavFragmentState.SuccessGetFromDb -> handelWeather(state.weatherEntity[0])
            is HomeFavFragmentState.IsLoading -> handleLoading(state.isLoading)
            is HomeFavFragmentState.ShowToast -> {
                requireActivity().showToast(state.message)
                getFromDb()
            }
            is HomeFavFragmentState.Init -> Unit
        }
    }

    private fun handelWeather(weatherEntity: WeatherEntity) {
        var mDigree = "0"
        Log.d("handelWeather", "handelWeather")
        binding.weatherRecyclerView.adapter?.let {
            if (it is HomeWeatherAdapter) {
                it.updateList(
                    useCase.invoke(),
                    weatherEntity.list.distinctBy { date -> Utils.convertDate(date.dt_txt) })
            }
        }

        homeViewModel.viewStatse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it)
                    mDigree = "${weatherEntity.list[0].main?.temp} °C"
                else mDigree = "${weatherEntity.list[0].main?.fahrenheit} °F"
            }
        })

        textView.text = weatherEntity.city.name!!.trim()
        binding.textTemp.text =
            if (useCase.invoke()) "${weatherEntity.list[0].main?.temp} °C" else "${weatherEntity.list[0].main?.fahrenheit} °F"
        binding.textTempDesc.text = weatherEntity.list[0].weather!![0].main
        binding.textHum.text = "${weatherEntity.list[0].main?.humidity} H"

        if (bundle == null) {
            listiners(weatherEntity)
        } else {
            arguments?.remove("weather")
            bundle = null
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.favIcon.gone()
            binding.loadingProgressBar.visible()
        } else {
            binding.favIcon.visible()
            binding.loadingProgressBar.gone()
        }
    }

    private fun getCurrentUserLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                homeViewModel.getWeatherData(
                    location?.longitude.toString(),
                    location?.latitude.toString()
                )
            }

    }


    fun checkLocationPermission(): Boolean {

        return if (activity?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
            }
            != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it, Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } == true) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(context)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(
                        R.string.ok,
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            activity?.let {
                                requestPermissions(
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    MY_PERMISSIONS_REQUEST_LOCATION
                                )
                            }
                        })
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                activity?.let {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION
                    )
                }
            }
            false
        } else {
            getCurrentUserLocation()
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (context?.let {
                            ContextCompat.checkSelfPermission(
                                it, Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        } == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        getCurrentUserLocation()

                    }
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.needs_location_for_better_service),
                        Toast.LENGTH_LONG
                    ).show()
                    Navigation.findNavController(binding.root).popBackStack()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}