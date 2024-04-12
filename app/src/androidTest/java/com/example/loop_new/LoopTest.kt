package com.example.loop_new

import android.app.Activity
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.loop_new.presentation.activity.MainActivity
import com.example.loop_new.presentation.screens.TestScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class LoopTest {


    @get:Rule
    val rule = createComposeRule()

    @Test
    fun clik() {
        rule.setContent { TestScreen() }

        rule.onNodeWithText("click").performClick()
    }
}