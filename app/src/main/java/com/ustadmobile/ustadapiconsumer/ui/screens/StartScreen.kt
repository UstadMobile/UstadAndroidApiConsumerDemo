package com.ustadmobile.ustadapiconsumer.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ustadmobile.ustadapiconsumer.GetOfflineAuthActivityResultContract

@Preview
@Composable
fun StartScreen(
    onClickAccountList: () -> Unit = {},
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = GetOfflineAuthActivityResultContract(),
        onResult = { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(context, "Got token: ${result.accountName} / ${result.authToken}",
                    Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(context, "User declined", Toast.LENGTH_LONG).show()
            }
        }
    )

    Column {
        Button(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = {
                launcher.launch(null)
            }
        ) {
            Text("Request Token")
        }
    }
}