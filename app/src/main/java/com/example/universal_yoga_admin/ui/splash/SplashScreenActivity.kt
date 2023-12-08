package com.example.universal_yoga_admin.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.universal_yoga_admin.preferences.PreferenceUtil
import com.example.universal_yoga_admin.ui.login.LoginActivity
import com.example.universal_yoga_admin.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (android.os.Build.VERSION.SDK_INT > 27) {
            window.setFlags(WindowManager.LayoutParams.FLAG_SCALED, WindowManager.LayoutParams.FLAG_SCALED)
        }
    }

    override fun onStart() {
        super.onStart()
        if (prefs.getUserId().isEmpty()){
            prefs.clearAllPrefs()
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
        } else{
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        }
    }
}