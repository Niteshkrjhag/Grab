package com.example.first_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.first_app.Screen.Splash
import com.example.first_app.presentation.mainActivity.MainActivity
import com.example.first_app.presentation.mainActivity.MainViewModel
import com.example.first_app.ui.theme.First_AppTheme
import kotlinx.coroutines.delay

class SplashActivity:ComponentActivity(){

    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{
           First_AppTheme{
                SplashScreen()
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    private fun SplashScreen(){
        val alpha = remember{
           Animatable(0f)
        }

        LaunchedEffect(key1 = true) {
            alpha.animateTo(1f,
                animationSpec = tween(1500, easing = FastOutLinearInEasing)
                )
            delay(2000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
     Splash(alpha)
    }
}
