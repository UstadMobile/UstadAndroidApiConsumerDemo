package com.ustadmobile.ustadapiconsumer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun StartScreen(
    onClickAccountList: () -> Unit = {},
) {
    Column {
        Button(onClick = onClickAccountList) {
            Text("Existing accounts")
        }
    }
}