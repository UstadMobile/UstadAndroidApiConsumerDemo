package com.ustadmobile.ustadapiconsumer

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

data class GetTokenResult(
    val resultCode: Int,
    val accountName: String?,
    val accountType: String?,
    val authToken: String?
)

class GetOfflineAuthActivityResultContract: ActivityResultContract<String?, GetTokenResult>() {
    /**
     * @param input where the desired endpoint servername is known, it can provided.
     */
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent("com.ustadmobile.AUTH_GET_TOKEN",
            Uri.parse("local-auth://${input ?: ""}"))
    }

    override fun parseResult(resultCode: Int, intent: Intent?): GetTokenResult {
        val addedName = intent?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        val addedType = intent?.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)
        val authToken = intent?.getStringExtra(AccountManager.KEY_AUTHTOKEN)

        return GetTokenResult(resultCode, addedName, addedType, authToken)
    }
}
