package com.example.calculator.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.calculator.R
import com.example.calculator.databinding.ActivityLoginBinding
import com.example.calculator.model.User
import com.example.calculator.utils.MyPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    var loginCredentials = HashMap<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        if(MyPreference.readPrefString(this,"userName")?.isNotEmpty() == true){
            startActivity(Intent(this,MainActivity::class.java))
        }
        mAuth = FirebaseAuth.getInstance()
        loginCredentials["admin@gmail.com"] = "Admin123"
        loginCredentials["user@gmail.com"] ="Password"
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
                    if (credentials.key == binding.email.text.toString() && credentials.value == binding.password.text.toString()){
                        binding.progressBar.visibility = View.GONE
                        addUserToDatabase(binding.userName.text.toString(),binding.email.text.toString(),binding.userName.text.toString())
                        MyPreference.writePrefString(this,"userName",binding.userName.text.toString())
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("isNowLoggedIn",true)
                        startActivity(intent)
                        return@setOnClickListener
                    }else{
                        hideKeyboard()
                        Snackbar.make(binding.root,getString(R.string.invalid_credentials),Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
        //google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.googleSignInBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val signInIntent = mGoogleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account = task.result
                    if (account != null) {
                        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
                        mAuth.signInWithCredential(credentials).addOnCompleteListener {
                            if (it.isSuccessful) {
                                account.displayName?.let { it1 -> account.email?.let { it2 ->
                                    addUserToDatabase(it1,
                                        it2,mAuth.uid)
                                } }
                                binding.progressBar.visibility = View.VISIBLE
                                val intent = Intent(this, MainActivity::class.java)
                                MyPreference.writePrefString(this,"userName",mAuth.uid.toString())
                                MyPreference.writePrefString(this,"loginFrom","google")
                                intent.putExtra("isNowLoggedIn",true)
                                startActivity(intent)
                            } else {
                                binding.progressBar.visibility = View.GONE
                                Snackbar.make(
                                    binding.root,
                                    it.exception.toString(),
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    } else {
                        Snackbar.make(
                            binding.root,
                            task.exception.toString(),
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
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

    private fun addUserToDatabase(name: String, email: String, uid: String?) {
        databaseRef = FirebaseDatabase.getInstance().getReference("user")
        databaseRef.child(uid.toString()).setValue(User(name, email, uid))
    }

}