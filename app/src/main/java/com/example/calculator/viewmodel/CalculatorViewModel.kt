package com.example.calculator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.model.CalculatorResponse
import com.example.calculator.network.CalculatorApi
import com.example.calculator.network.CalculatorRetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalculatorViewModel : ViewModel() {
    val mutableLiveData = MutableLiveData<CalculatorResponse>()

    fun getCalculatorObserver(): MutableLiveData<CalculatorResponse> {
        return mutableLiveData
    }

    fun apiCall() {
        viewModelScope.launch(Dispatchers.IO) {
            val instance =
                CalculatorRetrofitInstance.getRetroInstance().create(CalculatorApi::class.java)
            val response = instance.getUserData()
            mutableLiveData.postValue(response)
        }
    }
}