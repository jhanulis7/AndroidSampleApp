package com.mobis.cp.client.vm

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobis.cp.client.model.UiState
import com.mobis.cp.client.provider.AiAssistantProvider
import com.mobis.cp.client.provider.AiAssistantProviderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CpViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repositoryProvider: AiAssistantProviderRepository
) : ViewModel() {
    private val tag = "[ContentProvider] ${this::class.java.simpleName}"
    var uiState by mutableStateOf(UiState())

    private val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            uri?.let {
                Log.d(tag, "uri:$it contentObserver changed selfChange:$selfChange ")
                queryAgentStatus()
            }
        }
    }

    fun initialize() {
        registerContentProvider()
    }

    fun release() {
        unregisterContentProvider()
    }
    private fun registerContentProvider() {
        try {
            context.contentResolver.registerContentObserver(AiAssistantProvider.CONTENT_URI, false, contentObserver)
        } catch (e: SecurityException) {
            Log.e("ContentProvider", "$e")
        }
    }
    private fun unregisterContentProvider() {
        try {
            context.contentResolver.unregisterContentObserver(contentObserver)
        } catch (e: SecurityException) {
            Log.e("ContentProvider", "$e")
        }
    }

    fun queryAgentStatus() {
        val status = repositoryProvider.getAgentStatus()
        Log.d(tag, "queryAgentStatus() status: $status")
        uiState = uiState.copy(agentStatus = status)
    }
}