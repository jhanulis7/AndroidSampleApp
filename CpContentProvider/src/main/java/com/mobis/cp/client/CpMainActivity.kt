package com.mobis.cp.client

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mobis.cp.client.ui.screen.CpClientScreen
import com.mobis.cp.client.ui.theme.CpSampleTheme
import com.mobis.cp.client.vm.CpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CpMainActivity : ComponentActivity() {
    private val tag = "[ContentProvider] ${this::class.java.simpleName}"
    private val viewModel: CpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(tag, "onCreate()")

        super.onCreate(savedInstanceState)
        setContent {
            CpSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val onButton: () -> Unit = {
                        viewModel.queryAgentStatus()
                    }
                    CpClientScreen(
                        uiState = viewModel.uiState,
                        name = "Client Sample for AI Assistant",
                        modifier = Modifier.fillMaxWidth(),
                        onButton = onButton,
                    )
                }
            }
        }
    }
}