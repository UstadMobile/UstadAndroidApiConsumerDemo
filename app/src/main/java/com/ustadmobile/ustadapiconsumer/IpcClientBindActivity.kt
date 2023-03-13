package com.ustadmobile.ustadapiconsumer

import com.ustadmobile.httpoveripc.client.HttpOverIpcClient
import kotlinx.coroutines.flow.Flow

interface IpcClientBindActivity {

    val ipcClient: HttpOverIpcClient?

    val httpPort: Flow<Int>

}