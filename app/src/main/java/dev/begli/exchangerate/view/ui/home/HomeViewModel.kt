package dev.begli.exchangerate.view.ui.home

import androidx.lifecycle.*
import dev.begli.exchangerate.model.network.Resource
import dev.begli.exchangerate.model.responses.ExchangeRateResponse
import dev.begli.exchangerate.repositories.ExchangeRateRepository
import dev.begli.exchangerate.utils.Constants.Companion.ACCESS_TOKEN
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class HomeViewModel(private val repository: ExchangeRateRepository) : ViewModel() {
    private val _exchangeRates: MutableLiveData<Resource<ExchangeRateResponse>> = MutableLiveData()
    val exchangeRates: LiveData<Resource<ExchangeRateResponse>> get() = _exchangeRates

    fun getExchangeRates() = viewModelScope.launch {
        _exchangeRates.value = repository.getExchangeRates(ACCESS_TOKEN)
    }

}

class HomeViewModelFactory(private val repository: ExchangeRateRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}