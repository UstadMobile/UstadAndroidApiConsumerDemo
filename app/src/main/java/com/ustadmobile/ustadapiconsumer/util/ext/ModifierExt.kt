package com.ustadmobile.ustadapiconsumer.util.ext

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString

fun Modifier.copyClickable(
    text: String,
    clipboardManager: ClipboardManager,
    context: Context,
) : Modifier {

    return clickable {
        clipboardManager.setText(AnnotatedString(text))
        Toast.makeText(context, "Copied!", Toast.LENGTH_LONG).show()
    }
}