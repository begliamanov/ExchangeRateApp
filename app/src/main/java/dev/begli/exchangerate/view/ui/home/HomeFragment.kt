package dev.begli.exchangerate.view.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.begli.exchangerate.R
import dev.begli.exchangerate.databinding.ActivityMainBinding
import dev.begli.exchangerate.databinding.HomeFragmentBinding
import dev.begli.exchangerate.model.network.ExchangeRatesApi
import dev.begli.exchangerate.model.network.RemoteDataSource
import dev.begli.exchangerate.model.network.Resource
import dev.begli.exchangerate.repositories.ExchangeRateRepository
import dev.begli.exchangerate.utils.CookieBarNotify

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding

    private lateinit var cookieBarNotify: CookieBarNotify

    private val remoteDataSource = RemoteDataSource()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)

        // CookieBar notification init
        cookieBarNotify = CookieBarNotify(requireActivity())
        // ViewModel init
        val factory = HomeViewModelFactory(ExchangeRateRepository(remoteDataSource
                .buildApi(ExchangeRatesApi::class.java)))
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get exchange rates
        viewModel.getExchangeRates()

        // Observe exchange rates live data
        viewModel.exchangeRates.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    cookieBarNotify.success("Success!")
                }
                is Resource.Failure -> {
                    cookieBarNotify.error("Failure!")
                }
            }
        }
    }


}