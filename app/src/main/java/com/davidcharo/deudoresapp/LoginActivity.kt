package com.davidcharo.deudoresapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidcharo.deudoresapp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var loginBainding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBainding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBainding.root)
    }
}