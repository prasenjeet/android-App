package com.example.zfold5app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zfold5app.ui.screens.ZFoldApp
import com.example.zfold5app.ui.theme.ZFold5AppTheme
import com.example.zfold5app.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZFold5AppTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                val viewModel: MainViewModel = viewModel()
                ZFoldApp(
                    windowSizeClass = windowSizeClass,
                    activity = this,
                    viewModel = viewModel
                )
            }
        }
    }
}
