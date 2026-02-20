package com.oem.drivemode.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.oem.drivemode.sample.ui.TransactionTrackerScreen
import com.oem.drivemode.sample.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.oem.drivemode.sample.ui.theme.SampleTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: TransactionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleTheme {
                TransactionTrackerScreen(viewModel)
            }
        }
    }
}
