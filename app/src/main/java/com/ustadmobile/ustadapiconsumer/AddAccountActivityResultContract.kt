package com.ustadmobile.ustadapiconsumer

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

data class AddAccountResult(val accountName: String?, val accountType: String?)

class AddAccountActivityResultContract: ActivityResultContract<Intent, AddAccountResult>() {
    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): AddAccountResult {
        val addedName = intent?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        val addedType = intent?.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)

        return AddAccountResult(addedName, addedType)
    }
}