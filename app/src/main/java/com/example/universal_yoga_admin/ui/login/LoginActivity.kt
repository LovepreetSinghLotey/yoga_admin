package com.example.universal_yoga_admin.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.universal_yoga_admin.R
import com.example.universal_yoga_admin.databinding.ActivityLoginBinding
import com.example.universal_yoga_admin.extensions.hideLoader
import com.example.universal_yoga_admin.extensions.showLoader
import com.example.universal_yoga_admin.extensions.showSnackbar
import com.example.universal_yoga_admin.preferences.PreferenceUtil
import com.example.universal_yoga_admin.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    @Inject lateinit var preferenceUtil: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        handleGui()
    }

    private fun handleGui(){
        binding.go.setOnClickListener {
            login()
        }
    }

    fun login(){
        val userId: String = binding.userId.text.toString().trim()
        val password: String = binding.password.text.toString().trim()
        if (userId.isEmpty()) {
            showSnackbar("Please enter a valid userId")
            return
        }
        if (password.isEmpty() || password != "$userId@#") {
            showSnackbar("Please enter a valid password")
            return
        }
        showLoader()
        preferenceUtil.saveUserId(userId)
        hideLoader()
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }
}