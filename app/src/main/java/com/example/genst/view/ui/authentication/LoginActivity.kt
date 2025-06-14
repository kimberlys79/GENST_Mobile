package com.example.genst.view.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.genst.R
import com.example.genst.data.preference.UserPreference
import com.example.genst.data.preference.dataStore
import com.example.genst.data.result.Results
import com.example.genst.databinding.ActivityLoginBinding
import com.example.genst.view.ui.MainActivity
import com.example.genst.view.utils.FactoryViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val authViewModelViewModel by viewModels<AuthenticationViewModel> {
        FactoryViewModel.getInstance(this)
    }
    private val preference: UserPreference by lazy {
        UserPreference.getInstance(applicationContext.dataStore)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnMasukDgnEmail.setOnClickListener {
            textFieldWatcher()
            val badgeNumber = binding.etBadgeNumber.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etKataSandi.text.toString().trim()
            Log.d("LoginActivity", "Button Login Clicked with Badge Number: $badgeNumber, Email: $email, Password: $password")
            lifecycleScope.launch {
                try {
                    authViewModelViewModel.login(badgeNumber, email, password)
                    loginObserver()
                    Log.d("LoginActivity", "Button Login Clicked 2" )
                } catch (e: Exception) {
                    loginFailed()
                }
            }
        }
    }

    private fun loginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun loginFailed() {
        Toast.makeText(this, "Login Gagal, Silahkan Coba Lagi", Toast.LENGTH_SHORT).show()
    }

    private suspend fun loginObserver() {
        authViewModelViewModel.loginUserResponse.observe(this) { loginResponseResult ->
            when (loginResponseResult) {
                is Results.Success -> {
                    lifecycleScope.launch {
                        val loginData = loginResponseResult.data.loginResult
                        if (loginData?.token != null) {
                            Log.d("LoginActivity", "Login Success")
                            loginSuccess()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login data incomplete", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is Results.Error -> {
                    Toast.makeText(this, "Gagal Login", Toast.LENGTH_SHORT).show()
                    Log.e(
                        "LoginActivity",
                        "Gagal Masuk: ${loginResponseResult.error}"
                    )
                }
                is Results.Loading -> {
                }
            }
        }
    }

    private fun textFieldWatcher() {
        val textFields = listOf(
            binding.etBadgeNumber,
            binding.etEmail,
            binding.etKataSandi
        )
        for (textField in textFields) {
            textField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonSet()
                }
                override fun afterTextChanged(p0: Editable?) {}
            })
        }
    }

    private fun buttonSet() {
        val badgeNumber = binding.etBadgeNumber.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etKataSandi.text.toString()

        val isFieldFilled =
                    badgeNumber.isNotEmpty() &&
                    password.isNotEmpty() &&
                    email.isNotEmpty()
    }
}