package com.mobis.cp.client.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobis.cp.client.model.UiState
import com.mobis.cp.client.provider.AiAssistantProviderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CpViewModel @Inject constructor(
    private val repositoryProvider: AiAssistantProviderRepository
) : ViewModel() {
    private val tag = "[ContentProvider] ${this::class.java.simpleName}"
    var uiState by mutableStateOf(UiState())

    fun queryAgentStatus() {
        val status = repositoryProvider.getAgentStatus()
        Log.d(tag, "queryAgentStatus() status: $status")
        uiState = uiState.copy(agentStatus = status)
    }

}