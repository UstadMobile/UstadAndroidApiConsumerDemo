package com.ustadmobile.ustadapiconsumer

import android.os.Bundle
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
import com.ustadmobile.ustadapiconsumer.ui.screens.AccountDetailScreen
import com.ustadmobile.ustadapiconsumer.ui.screens.AccountListScreen
import com.ustadmobile.ustadapiconsumer.ui.screens.StartScreen
import com.ustadmobile.ustadapiconsumer.ui.theme.UstadApiConsumerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UstadApiConsumerTheme {
                AppNavHost()
            }
        }
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