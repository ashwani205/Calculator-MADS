package com.example.calculator.network

import com.example.calculator.model.CalculatorResponse
import retrofit2.http.GET

interface CalculatorApi {


    @GET("spreadsheets/d/e/2PACX-1vSxcJxWFceeqDkfV-1x_WeSKt-0eqp99ya6RNmt1hShTbX3U8vur8DGtmhtb3msa36kVnOk9gvQ5Cab/pubhtml")
suspend fun getUserData(

):CalculatorResponse
}