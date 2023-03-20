package com.example.myapp.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.model.ExchangeRatesApi
import kotlinx.coroutines.launch

sealed interface ExchangeRatesUIState {
    object Loading : ExchangeRatesUIState
    object Error : ExchangeRatesUIState
    object Success : ExchangeRatesUIState
}

class ExchangeRateViewModel: ViewModel() {
    var eurInput by mutableStateOf("")
    var czkOutput by mutableStateOf(0.0)
        private set
    var czkRate by mutableStateOf(0.0f)
        private set
    var exchangeRatesUIState by mutableStateOf<ExchangeRatesUIState>(ExchangeRatesUIState.Loading)
        private set



    init {
        getExchangeRateForCzk()
    }

    fun changeCzk(newValue : String) {
        eurInput = newValue

    }

    fun convertEurtoCzk() {
        val euros = eurInput.toDoubleOrNull() ?: 0.0
        czkOutput = euros * czkRate

    }

    private fun getExchangeRateForCzk() {
        viewModelScope.launch {
            var exchangeRatesApi: ExchangeRatesApi? = null

            try {
                exchangeRatesApi = ExchangeRatesApi.getInstance()
                val exchangeRates = exchangeRatesApi!!.getRates()
                czkRate = exchangeRates.rates.CZK
                if(exchangeRates.success) {
                    czkRate = exchangeRates.rates.CZK
                    exchangeRatesUIState = ExchangeRatesUIState.Success
                }
                else {
                    exchangeRatesUIState = ExchangeRatesUIState.Error
                }
            } catch (e: Exception) {
                czkRate = 0.0f
                exchangeRatesUIState = ExchangeRatesUIState.Error
            }

        }
    }
}