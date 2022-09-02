package com.example.calculator.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.R
import com.example.calculator.databinding.ActivityLoginBinding
import com.example.calculator.model.CalculatorResponse
import com.example.calculator.viewmodel.CalculatorViewModel
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: CalculatorViewModel

    var loginCredentials = HashMap<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]
        viewModel.getCalculatorObserver().observe(this) { render(it) }
        loginCredentials["Admin"] = "Admin123"
        loginCredentials["userName"] ="Password"
        binding.loginButton.setOnClickListener {
            if(binding.userName.text.isNullOrEmpty()){
                hideKeyboard()
                binding.userName.error=getString(R.string.user_name_empty)
                    Snackbar.make(binding.root,R.string.user_name_empty,Snackbar.LENGTH_SHORT).show()
            }else if(binding.password.text.isNullOrEmpty()){
                hideKeyboard()
                binding.password.error = getString(R.string.password_empty)
                Snackbar.make(binding.root,getString(R.string.password_empty),Snackbar.LENGTH_SHORT).show()
            }else{
                loginCredentials.forEach { credentials->
                    if (credentials.key == binding.userName.text.toString() && credentials.value == binding.password.text.toString()){
                        binding.progressBar.visibility = View.VISIBLE
                        viewModel.apiCall()
//                        startActivity(Intent(this,MainActivity::class.java))

                    }else{
                        hideKeyboard()
                        Snackbar.make(binding.root,getString(R.string.invalid_credentials),Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun render(calculatorResponse: CalculatorResponse?) {
        binding.progressBar.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}