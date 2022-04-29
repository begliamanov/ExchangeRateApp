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
import dev.begli.exchangerate.model.data.SharedPref
import dev.begli.exchangerate.model.network.ExchangeRatesApi
import dev.begli.exchangerate.model.network.RemoteDataSource
import dev.begli.exchangerate.model.network.Resource
import dev.begli.exchangerate.repositories.ExchangeRateRepository
import dev.begli.exchangerate.utils.Constants.Companion.FIFTEEN_SECONDS
import dev.begli.exchangerate.utils.Constants.Companion.FIVE_SECONDS
import dev.begli.exchangerate.utils.Constants.Companion.THREE_SECONDS
import dev.begli.exchangerate.utils.CookieBarNotify
import dev.begli.exchangerate.utils.DateConverter
import dev.begli.exchangerate.utils.Exchange
import dev.begli.exchangerate.view.adapters.ExchangeRatesAdapter
import java.sql.Time
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding

    private lateinit var cookieBarNotify: CookieBarNotify
    private lateinit var exchangeRatesAdapter: ExchangeRatesAdapter
    private lateinit var rates: Array<String>
    private lateinit var sharedPref: SharedPref
    private lateinit var timer: Timer

    private val remoteDataSource = RemoteDataSource()



    private val refreshTimes = arrayOf("3 seconds", "5 seconds", "15 seconds")
    private val refreshTimesLong = arrayOf(THREE_SECONDS, FIVE_SECONDS, FIFTEEN_SECONDS)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)

        // CookieBar notification init
        cookieBarNotify = CookieBarNotify(requireActivity())
        // Shared Preferences init
        sharedPref = SharedPref(requireContext())
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
        binding.refreshTime.text = getExchangeRates()
        binding.defaultRateTextView.text = sharedPref.defaultRate()
        binding.refreshTextView.text = refreshTimes[refreshTimesLong.indexOf(sharedPref.refreshInterval())]

        // default rate setting card
        binding.defaultRateCard.setOnClickListener {
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.MaterialAlertDialog_Rounded
            )
                .setTitle("Select a base rate")
                .setItems(rates) { dialog, which ->
                    sharedPref.setDefaultRate(rates[which])
                    binding.defaultRateTextView.text = sharedPref.defaultRate()
                    binding.refreshTime.text = getExchangeRates()
                }
                .show()
        }

        // default refresh tome setting card
        binding.refreshCard.setOnClickListener {
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.MaterialAlertDialog_Rounded
            )
                .setTitle("Select a refresh rate")
                .setItems(refreshTimes) { dialog, which ->
                    sharedPref.setRefreshInterval(refreshTimesLong[which])
                    binding.refreshTextView.text = refreshTimes[which]
                    timer.cancel()
                    setTimer()
                }
                .show()
        }

        // Run following function periodically every x seconds.
        setTimer()

        // Observe exchange rates live data
        viewModel.exchangeRates.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val updateRates = Exchange().exchangeAllRates(it.value.rates[sharedPref.defaultRate()]!!, it.value.rates)
                    // Populating rates adapter
                    exchangeRatesAdapter.setDataSet(updateRates)
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

    private fun setTimer() {
        timer = Timer()
        timer.scheduleAtFixedRate( object : TimerTask() {
            override fun run() {
                // Get exchange rates
                Handler(Looper.getMainLooper()).post { binding.refreshTime.text = getExchangeRates() }
            }
        }, 0, sharedPref.refreshInterval())
    }


}