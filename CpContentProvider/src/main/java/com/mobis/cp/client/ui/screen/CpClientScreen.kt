package com.mobis.cp.client.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobis.cp.client.constant.MsgCommand
import com.mobis.cp.client.model.UiState
import com.mobis.cp.client.vm.CpViewModel

@Composable
fun CpClientScreen(
    uiState: UiState,
    name: String,
    modifier: Modifier = Modifier,
    onButton: (Int, Int, String) -> Unit
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
            onButton.invoke(MsgCommand.MSG_REQ_VR_STATUS.command, 100, "Hello MOBIS!")
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