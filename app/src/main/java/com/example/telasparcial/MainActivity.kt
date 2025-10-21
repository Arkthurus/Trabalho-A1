package com.example.telasparcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.telasparcial.ui.nav.AppNav
import com.example.telasparcial.ui.theme.TelasParcialTheme
import com.example.telasparcial.ui.viewmodel.AuthViewModel


class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TelasParcialTheme {
                AppNav(authViewModel)
            }
        }
    }
}