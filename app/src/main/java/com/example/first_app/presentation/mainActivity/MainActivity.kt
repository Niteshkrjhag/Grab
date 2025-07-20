package com.example.first_app.presentation.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
//import com.example.first_app.ChatBot.ChatViewModel.ChatViewModel
import com.example.first_app.Login_auth.LoginViewModel
import com.example.first_app.googleSignIn.GoogleSignInViewModel
import com.example.first_app.navigation.Navigation
import com.example.first_app.ui.theme.First_AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen().apply {
            setKeepOnScreenCondition(condition = { viewModel.splashCondition.value })
        }
//        val viewModel = ViewModelProvider(this).get(view_test_model::class.java)
        val authViewModel:LoginViewModel by viewModels()
//        val chatViewModel= ViewModelProvider(this)[ChatViewModel::class.java]
        val googleSignInViewModel = GoogleSignInViewModel()

        setContent {

            First_AppTheme {
                val isSystemInDarkTheme = isSystemInDarkTheme()
                val context = LocalContext.current
                val systemController = rememberSystemUiController()
                SideEffect {
                    systemController.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkTheme
                    )
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

//                      val startDestination = viewModel.startDestination.toString()
                      Navigation(viewModel.startDestination.value, authViewModel = authViewModel,context,googleSignInViewModel)
                }
            }
        }
    }

}


