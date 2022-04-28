package dev.begli.exchangerate.view.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.begli.exchangerate.R
import dev.begli.exchangerate.databinding.HomeFragmentBinding
import dev.begli.exchangerate.model.network.ExchangeRatesApi
import dev.begli.exchangerate.model.network.RemoteDataSource
import dev.begli.exchangerate.model.network.Resource
import dev.begli.exchangerate.repositories.ExchangeRateRepository
import dev.begli.exchangerate.utils.Constants.Companion.FIVE_SECONDS
import dev.begli.exchangerate.utils.CookieBarNotify
import dev.begli.exchangerate.utils.DateConverter
import dev.begli.exchangerate.view.adapters.ExchangeRatesAdapter
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding

    private lateinit var cookieBarNotify: CookieBarNotify
    private lateinit var exchangeRatesAdapter: ExchangeRatesAdapter
    private lateinit var rates: Array<String>

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
        // Get exchange rates
        val currentTime = getExchangeRates()
        binding.refreshTime.text = "Latest update: $currentTime"

        binding.defaultRateCard.setOnClickListener {
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.MaterialAlertDialog_Rounded
            )
                .setTitle("Select a base rate")
                .setItems(rates) { dialog, which ->
                    binding.defaultRateTextView.text = rates[which]

                }
                .show()
        }

        // Run following function periodically every x seconds.
        Timer().scheduleAtFixedRate( object : TimerTask() {
            override fun run() {
                // Get exchange rates
                val currentTime = getExchangeRates()
                Handler(Looper.getMainLooper()).post { binding.refreshTime.text = "Latest update: $currentTime" }

            }
        }, 0, FIVE_SECONDS)

        // Observe exchange rates live data
        viewModel.exchangeRates.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    // Populating rates adapter
                    exchangeRatesAdapter.setDataSet(it.value.rates)
                    // declare rates
                    rates = it.value.rates.keys.toTypedArray()
                }
                is Resource.Failure -> {
                    cookieBarNotify.error(it.message ?: "Unexpected error")
                }
            }
        }
    }

    private fun getExchangeRates(): String? {
        viewModel.getExchangeRates()
        // Update refresh time
        return DateConverter().format(Date(), "hh:mm:ss")
    }


}