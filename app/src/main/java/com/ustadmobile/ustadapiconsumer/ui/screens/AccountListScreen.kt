package com.ustadmobile.ustadapiconsumer.ui.screens

import android.accounts.Account
import android.accounts.AccountManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
@Deprecated("Not using accountmanager for this anymore")
fun AccountListScreen(
    onClickAccount: (Account) -> Unit = {},
) {
    val context = LocalContext.current
    val accounts: List<Account> by remember {
        mutableStateOf(
            AccountManager.get(context)
                .getAccountsByType("com.ustadmobile")
                .toList())
    }

    Column {
        Text("Accounts")

        accounts.forEach { account ->
            TextButton(onClick = { onClickAccount(account) }) {
                Text(account.name)
            }
        }
    }
}