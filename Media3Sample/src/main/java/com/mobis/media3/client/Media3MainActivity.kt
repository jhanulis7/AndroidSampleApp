package com.mobis.media3.client

import android.content.ComponentName
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
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.mobis.cp.client.ui.theme.CpSampleTheme
import com.mobis.media3.client.service.PlaybackService
import com.mobis.media3.client.ui.screen.Media3ClientScreen
import com.mobis.media3.client.vm.Media3ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Media3MainActivity : ComponentActivity() {
    private val tag = "[Media3] ${this::class.java.simpleName}"
    private val viewModel: Media3ViewModel by viewModels()
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null
    private lateinit var playerView: PlayerView
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
                        //viewModel.queryAgentStatus()
                        //mediaController.play()
                        controller?.play()
                    }
                    Media3ClientScreen(
                        uiState = viewModel.uiState,
                        name = "Client Sample for AI Assistant",
                        modifier = Modifier.fillMaxWidth(),
                        onButton = onButton,
                    )
                }
            }
        }
        viewModel.initialize()
    }


    override fun onStart() {
        super.onStart()
        initializeController()
    }

    private fun initializeController() {
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this,sessionToken).buildAsync()
        controllerFuture.addListener({
//            val controller = controllerFuture.get()
//            //controller.setMediaItem(mediaItem)
//            controller.prepare()
//            controller.play()
        }, MoreExecutors.directExecutor())
    }
    private fun finalizeController() {
        MediaController.releaseFuture(controllerFuture)
        mediaController = null
    }

    override fun onDestroy() {
        super.onDestroy()
        finalizeController()
        viewModel.release()
    }
}