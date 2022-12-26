package com.ustadmobile.ustadapiconsumer.ui.screens

import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import com.ustadmobile.ustadapiconsumer.AccountActivityResultContract

/**
 * Requesting an auth token for an existing account flow is as per:
 *
 *   https://developer.android.com/training/id-auth/authenticate#RequestToken
 */
@Composable
fun AccountDetailScreen(
    accountName: String = "",
) {


    var authToken : String? by rememberSaveable(inputs = arrayOf(accountName)){ mutableStateOf(null) }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = AccountActivityResultContract(),
        onResult ={ resultCode ->
            if(resultCode == Activity.RESULT_OK) {
                //When the result from launching the intent is OK, then we can ask accountmanager again
                // for the token for the given account.
                Log.d("ApiConsumer", "Intent says: OK - now ask AccountManager again")

                val accountManager = AccountManager.get(context)
                val account = accountManager.getAccountsByType("com.ustadmobile").first {
                    it.name == accountName
                }

                accountManager.getAuthToken(account, "", bundleOf(), false, {
                    val result = it.result
                    val token = result.getString(AccountManager.KEY_AUTHTOKEN)
                    authToken = token
                }, null)
            }


        }
    )

    Column {
        Text(accountName)

        Text("Auth token: ${if(authToken != null) authToken else "(none)"}")

        Button(onClick = {
            val accountManager = AccountManager.get(context)
            val account = accountManager.getAccountsByType("com.ustadmobile").first {
                it.name == accountName
            }


            accountManager.getAuthToken(account, "", bundleOf(), false, {

                val intent: Intent? = it.result.getParcelable(AccountManager.KEY_INTENT)
                if(intent != null) {
                    //We have been given an intent that we need to launch - user must grant permission in app
                    launcher.launch(intent)
                }else {
                    //We already have permission, display the token
                    authToken = it.result.getString(AccountManager.KEY_AUTHTOKEN)
                }
            }, null)
        }) {
            Text("Get token")
        }
    }





}