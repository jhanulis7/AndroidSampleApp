package com.mobis.media3.client.vm

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobis.media3.client.model.Media3UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class Media3ViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val tag = "[Media3] ${this::class.java.simpleName}"
    var uiState by mutableStateOf(Media3UiState())

    fun initialize() {
    }

    fun release() {
    }

}