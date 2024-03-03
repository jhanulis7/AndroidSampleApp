package com.mobis.cp.client

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mobis.cp.client.constant.MsgCommand
import com.mobis.cp.client.ui.screen.CpClientScreen
import com.mobis.cp.client.ui.theme.CpSampleTheme
import com.mobis.cp.client.vm.CpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CpMainActivity : ComponentActivity() {
    private val tag = "[AIDL-Client] ${this::class.java.simpleName}"
    private val viewModel: CpViewModel by viewModels()

    // messenger for service
    lateinit var requestMessenger: Messenger

    // messenger to fetch reply
    lateinit var receivedMessenger: Messenger

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(tag, "onCreate()")
        bindServiceConnection()

        super.onCreate(savedInstanceState)
        setContent {
            CpSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val onButton: (Int, Int, String) -> Unit = { command, data1, data2 ->
                        viewModel.sendMessage(
                            requestMessenger = requestMessenger,
                            receivedMessenger = receivedMessenger,
                            command = command,
                            data1 = data1,
                            data2 = data2
                        )
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

    private fun bindServiceConnection() {
        Log.d(tag, "bindServiceConnection()")
        val intent = Intent()
        intent.component = ComponentName("ai.umos.ivi.edith.sw", "ai.umos.ivi.edith.service.AiAssistantService")
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            Log.d(tag, "[Messenger] Service connected")
            requestMessenger = Messenger(iBinder)
            receivedMessenger = Messenger(ReceivedMessageHandler())
            Toast.makeText(applicationContext, "[Messenger] bind the service", Toast.LENGTH_SHORT)
                .show()
            viewModel.sendMessage(
                requestMessenger = requestMessenger,
                receivedMessenger = receivedMessenger,
                command = MsgCommand.MSG_REQ_CONNECTION.command
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(tag, "[Messenger] Service not connected! \nOpen remote app..")
            Toast.makeText(applicationContext, "[Messenger] Unable to bind the service", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @SuppressLint("HandlerLeak")
    inner class ReceivedMessageHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.i(tag, "[Messenger] Client Message received at activity")
            when (msg.what) {
                MsgCommand.MSG_NOTIFICATION_VR_START.command -> {
                    Log.i(tag, "[Messenger][Client] Message received MSG_SET_VR_START")
                    viewModel.uiState = viewModel.uiState.copy(agentStatus = "MSG_SET_VR_START")
                }
                MsgCommand.MSG_NOTIFICATION_VR_STOP.command -> {
                    Log.i(tag, "[Messenger][Client] Message received MSG_SET_VR_STOP")
                    viewModel.uiState = viewModel.uiState.copy(agentStatus = "MSG_SET_VR_STOP")
                }
                MsgCommand.MSG_RESP_VR_STATUS.command -> {
                    val data = msg.data.getString("status")
                    Log.i(tag, "[Messenger][Client] Message received MSG_RESP_VR_STATUS - $$data")
                    data?.let {
                        viewModel.uiState = viewModel.uiState.copy(agentStatus = it)
                    }
                }
                else -> {}
            }
        }
    }
}