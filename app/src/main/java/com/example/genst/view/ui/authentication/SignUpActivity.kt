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
import com.example.genst.data.result.Results
import com.example.genst.databinding.ActivitySignUpBinding
import com.example.genst.injection.Data
import com.example.genst.view.ui.MainActivity
import com.example.genst.view.utils.FactoryViewModel
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private val authViewModelViewModel by viewModels<AuthenticationViewModel> {
        FactoryViewModel.getInstance(this)
    }
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(binding.main.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textFieldWatcher()

        registerObserver()

        binding.btnDaftarDgnEmail.setOnClickListener {
            val name = binding.etNama.text.toString().trim()
            val badgeNumber = binding.etBadgeNumber.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            authViewModelViewModel.createNewUser(
                name,
                badgeNumber,
                email,
                password
            )
        }
    }

    private fun registerObserver() {
        authViewModelViewModel.registerUserResponse.observe(this) { registerResponseResult ->
            when (registerResponseResult) {
                is Results.Success -> {
                    registerSuccess()
                }

                is Results.Error -> {
                    Toast.makeText(this, "Gagal Melakukan Registrasi", Toast.LENGTH_SHORT).show()
                    Log.e(
                        "SignUpActivity",
                        "Gagal Registrasi: ${registerResponseResult.error}"
                    )
                }

                is Results.Loading -> {
                }
                else -> {
                }
            }
        }
    }

    private fun textFieldWatcher() {
        val textFields = listOf(
            binding.etNama,
            binding.etBadgeNumber,
            binding.etEmail,
            binding.etPassword
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
        val name = binding.etNama.text.toString()
        val badgeNumber = binding.etBadgeNumber.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        val isFieldFilled =
            name.isNotEmpty() &&
                    badgeNumber.isNotEmpty() &&
                    email.isNotEmpty() &&
                    password.isNotEmpty()


        binding.btnDaftarDgnEmail.isEnabled = isFieldFilled
    }

    private fun registerSuccess() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun registerFailed() {
        Toast.makeText(this, "Registrasi Gagal, Silahkan Coba Lagi", Toast.LENGTH_SHORT).show()
    }
}