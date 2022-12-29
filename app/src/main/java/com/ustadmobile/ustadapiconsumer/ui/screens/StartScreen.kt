package com.ustadmobile.ustadapiconsumer.ui.screens

import android.accounts.AccountManager
import android.content.Intent
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
import androidx.core.os.bundleOf
import com.ustadmobile.ustadapiconsumer.AddAccountActivityResultContract

@Preview
@Composable
fun StartScreen(
    onClickAccountList: () -> Unit = {},
) {
    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(
        contract = AddAccountActivityResultContract(),
        onResult = { addAccountResult ->
            //Now get auth token
            val accountManager = AccountManager.get(context)
            val account = accountManager.getAccountsByType("com.ustadmobile").first {
                it.name == addAccountResult.accountName
            }

            accountManager.getAuthToken(account, "", bundleOf(), false, {
                val authToken = it.result.getString(AccountManager.KEY_AUTHTOKEN)
                Toast.makeText(context, "Got account: ${account.name} / $authToken",
                    Toast.LENGTH_LONG).show()
            }, null)
        }
    )


    Column {
        Button(
            onClick = onClickAccountList,
            modifier = Modifier.padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text("Existing accounts")
        }

        Button(
            modifier = Modifier.padding(16.dp)
                .fillMaxWidth(),
            onClick = {
                AccountManager.get(context).addAccount("com.ustadmobile", "",
                    arrayOf(), bundleOf(), null, {
                        val result = it.getResult()
                        val intent: Intent? = result.getParcelable(AccountManager.KEY_INTENT)
                        if(intent != null) {
                            launcher.launch(intent)
                        }

                    }, null)
            }
        ) {
            Text("Add account")
        }
    }
}