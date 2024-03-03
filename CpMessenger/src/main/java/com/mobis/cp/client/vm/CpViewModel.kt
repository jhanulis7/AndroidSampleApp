package com.mobis.cp.client.vm

import android.os.Bundle
import android.os.Message
import android.os.Messenger
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobis.cp.client.CpMainActivity
import com.mobis.cp.client.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CpViewModel @Inject constructor() : ViewModel() {
    private val tag = "[AIDL-Client] ${this::class.java.simpleName}"
    var uiState by mutableStateOf(UiState())

    fun sendMessage(
        requestMessenger: Messenger,
        receivedMessenger: Messenger,
        command: Int,
        data1: Int? = null,
        data2: String? = null
    ) {
        val msg = Message.obtain(null, command, 0, 0).apply {
            data = Bundle().apply {
                data1?.let {
                    putInt("data1", it)
                }
                data2?.let {
                    putString("data2", it)
                }
            }
            replyTo = receivedMessenger
        }
        try {
            Log.i(tag, "[Messenger][Client] Message sent")
            requestMessenger.send(msg)
        } catch (e: Exception) {
            Log.e(tag, "[Messenger][Client] Error ${e.message}")
        }
    }
}