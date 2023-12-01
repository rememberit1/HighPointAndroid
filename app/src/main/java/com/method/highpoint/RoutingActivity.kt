package com.method.highpoint

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class RoutingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }
        val sharedPreference =  getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        if(sharedPreference.getBoolean("TUTORIAL_COMPLETE", false)){
            goToMainActivity()
        } else {
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    private fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        val sharedPreference =  getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean("TUTORIAL_COMPLETE", true)
        editor.commit()
        startActivity(intent)
    }
}