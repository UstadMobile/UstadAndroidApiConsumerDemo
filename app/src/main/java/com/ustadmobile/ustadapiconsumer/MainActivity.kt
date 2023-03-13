package com.ustadmobile.ustadapiconsumer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ustadmobile.httpoveripc.client.HttpOverIpcClient
import com.ustadmobile.httpoveripc.client.HttpOverIpcProxy
import com.ustadmobile.ustadapiconsumer.ui.screens.AccountDetailScreen
import com.ustadmobile.ustadapiconsumer.ui.screens.AccountListScreen
import com.ustadmobile.ustadapiconsumer.ui.screens.StartScreen
import com.ustadmobile.ustadapiconsumer.ui.theme.UstadApiConsumerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import rawhttp.core.RawHttp

class MainActivity : ComponentActivity(), IpcClientBindActivity {

    private var bound: Boolean = false

    private var mIpcClient: HttpOverIpcClient? = null

    override val ipcClient: HttpOverIpcClient?
        get() = mIpcClient

    private var mHttpOverIpcProxy: HttpOverIpcProxy? = null

    private val proxyPortFlow = MutableStateFlow(0)

    override val httpPort: Flow<Int> = proxyPortFlow.asStateFlow()

    private val mConnection = object: ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mIpcClient = HttpOverIpcClient(service).also {
                mHttpOverIpcProxy = HttpOverIpcProxy(it, RawHttp())
                mHttpOverIpcProxy?.start()
                proxyPortFlow.value = mHttpOverIpcProxy?.listeningPort ?: 0
            }

            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mHttpOverIpcProxy?.stop()
            proxyPortFlow.value = 0
            mIpcClient?.close()
            mIpcClient = null
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UstadApiConsumerTheme {
                AppNavHost()
            }
        }

        Intent("oneroster").also { intent ->
            intent.`package` = "com.toughra.ustadmobile"
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }
    override fun onDestroy() {
        if(bound){
            unbindService(mConnection)
        }

        super.onDestroy()
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "start"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("start") { navEntry ->
            StartScreen(
                onClickAccountList = { navController.navigate("accountlist")}
            )
        }

        composable("accountlist") { navEntry ->
            AccountListScreen(
                onClickAccount = {
                    navController.navigate("accountdetail/${it.name}")
                }
            )
        }

        composable(
            "accountdetail/{accountName}",
            arguments = listOf(navArgument("accountName") { type = NavType.StringType })
        ){ navEntry ->
            AccountDetailScreen(accountName = navEntry.arguments?.getString("accountName") ?: "")
        }
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UstadApiConsumerTheme {
        Greeting("Android")
    }
}