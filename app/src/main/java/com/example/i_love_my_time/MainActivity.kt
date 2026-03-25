package com.example.i_love_my_time

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.i_love_my_time.ui.AplicacionILoveMyTime
import com.example.i_love_my_time.ui.theme.I_love_my_timeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            I_love_my_timeTheme {
                AplicacionILoveMyTime()
            }
        }

    }
}