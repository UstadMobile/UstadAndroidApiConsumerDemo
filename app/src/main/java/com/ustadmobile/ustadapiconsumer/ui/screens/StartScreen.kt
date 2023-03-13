package com.ustadmobile.ustadapiconsumer.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ustadmobile.httpoveripc.core.SimpleTextRequest
import com.ustadmobile.httpoveripc.core.ext.asSimpleTextResponse
import com.ustadmobile.ustadapiconsumer.GetOfflineAuthActivityResultContract
import com.ustadmobile.ustadapiconsumer.GetTokenResult
import com.ustadmobile.ustadapiconsumer.IpcClientBindActivity
import com.ustadmobile.ustadapiconsumer.util.ext.copyClickable
import com.ustadmobile.ustadapiconsumer.util.ext.getActivityContext
import io.ktor.http.*
import kotlinx.coroutines.launch
import rawhttp.core.RawHttp

@Preview
@Composable
fun StartScreen(
    onClickAccountList: () -> Unit = {},
) {
    val context = LocalContext.current

    val ipcActivity = (context.getActivityContext() as IpcClientBindActivity)

    val clientPort: Int by ipcActivity.httpPort.collectAsState(initial = 0)

    var tokenResult: GetTokenResult? by rememberSaveable {
        mutableStateOf(null)
    }

    val rawHttp = remember {
        RawHttp()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = GetOfflineAuthActivityResultContract(),
        onResult = { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                tokenResult = result
                Toast.makeText(context, "Got token: ${result.accountName} / ${result.authToken}",
                    Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(context, "User declined", Toast.LENGTH_LONG).show()
            }
        }
    )

    val scope = rememberCoroutineScope()

    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column {
        Text(
            text ="Token: ${tokenResult?.authToken}",
            modifier = Modifier
                .copyClickable(tokenResult?.authToken ?:"", clipboardManager, context)
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
        )

        Text(
            text = "Account name: ${tokenResult?.accountName}",
            modifier = Modifier
                .copyClickable(tokenResult?.accountName ?:"", clipboardManager, context)
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
        )
        Text(
            text = "User sourcedId: ${tokenResult?.sourcedId}",
            modifier = Modifier
                .copyClickable(tokenResult?.sourcedId ?:"", clipboardManager, context)
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
        )
        Text(
            text = "Endpoint: ${tokenResult?.endpointUrl}",
            modifier = Modifier
                .copyClickable(tokenResult?.endpointUrl ?:"", clipboardManager, context)
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
        )

        Text(
            text = "HTTP Proxy: ${if(clientPort != 0) "http://localhost:$clientPort/" else "binding..."}",
            modifier = Modifier
                .copyClickable(tokenResult?.accountName ?:"", clipboardManager, context)
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
        )



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

        Button(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = {
                scope.launch {
                    val ipcClient = (context.getActivityContext() as IpcClientBindActivity).ipcClient
                    val getClassesUrl = URLBuilder().takeFrom(
                        tokenResult?.endpointUrl ?: ""
                    ).apply {
                        encodedPath = "${encodedPath}api/oneroster/users/${tokenResult?.sourcedId}/classes"
                    }
                    .build()
                    val response = ipcClient?.send(SimpleTextRequest(
                        method = SimpleTextRequest.Method.GET,
                        url = getClassesUrl,
                        headers = mapOf("auth-token" to (tokenResult?.authToken ?: ""))
                    ).toRawHttpRequest(rawHttp))?.asSimpleTextResponse()
                    println(response?.responseBody)
               }
            }
        ) {
            Text("Get class list for user")
        }

    }
}