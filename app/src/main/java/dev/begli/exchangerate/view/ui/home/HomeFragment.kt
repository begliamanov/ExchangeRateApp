package dev.begli.exchangerate.view.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dev.begli.exchangerate.R
import dev.begli.exchangerate.databinding.ActivityMainBinding
import dev.begli.exchangerate.databinding.HomeFragmentBinding
import dev.begli.exchangerate.model.network.ExchangeRatesApi
import dev.begli.exchangerate.model.network.RemoteDataSource
import dev.begli.exchangerate.model.network.Resource
import dev.begli.exchangerate.repositories.ExchangeRateRepository
import dev.begli.exchangerate.utils.Constants.Companion.FIVE_SECONDS
import dev.begli.exchangerate.utils.CookieBarNotify
import dev.begli.exchangerate.view.adapters.ExchangeRatesAdapter
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding

    private lateinit var cookieBarNotify: CookieBarNotify

    private val remoteDataSource = RemoteDataSource()

    private lateinit var exchangeRatesAdapter: ExchangeRatesAdapter

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

        // Initialize adapter
        binding.ratesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false)
            exchangeRatesAdapter = ExchangeRatesAdapter(requireContext())
            adapter = exchangeRatesAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Run following function periodically every x seconds.
        Timer().scheduleAtFixedRate( object : TimerTask() {
            override fun run() {
                // Get exchange rates
                viewModel.getExchangeRates()
            }
        }, 0, FIVE_SECONDS)

        // Observe exchange rates live data
        viewModel.exchangeRates.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    // Populating rates adapter
                    exchangeRatesAdapter.setDataSet(it.value.rates)
                }
                is Resource.Failure -> {
                    cookieBarNotify.error("Failure!")
                }
            }
        }
    }


}