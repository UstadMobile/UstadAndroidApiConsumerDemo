package com.ustadmobile.ustadapiconsumer

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract


class AccountActivityResultContract : ActivityResultContract<Intent, Int>() {

    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return resultCode
    }
}