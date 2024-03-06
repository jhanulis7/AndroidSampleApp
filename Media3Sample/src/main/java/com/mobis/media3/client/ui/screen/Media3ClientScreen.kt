package com.mobis.media3.client.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.mobis.media3.client.model.Media3UiState

@Composable
fun Media3ClientScreen(
    uiState: Media3UiState,
    name: String,
    modifier: Modifier = Modifier,
    onButton: () -> Unit
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "$name!",
            fontSize = 40.sp,
        )
        Spacer(modifier = Modifier.padding(top = 100.dp))
        Button(onClick = {
            onButton.invoke()
        }) {
            Text(text = "GET VR Status")
        }
        Spacer(modifier = Modifier.padding(top = 30.dp))
        Row {
            Text(
                text = "Agent Status: ",
                fontSize = 40.sp,
            )
            Text(
                text = uiState.agentStatus,
                fontSize = 40.sp,
            )
        }
    }
}