package com.example.telasparcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.telasparcial.ui.nav.AppNav
import com.example.telasparcial.ui.theme.TelasParcialTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TelasParcialTheme {
                AppNav()
            }
        }
    }
}