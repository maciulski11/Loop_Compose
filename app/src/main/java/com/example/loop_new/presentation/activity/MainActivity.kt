package com.example.loop_new.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.loop_new.navigation.NavigationScreens
import com.example.loop_new.ui.theme.Loop_NewTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Loop_NewTheme {

                NavigationScreens()
            }
        }
    }
}