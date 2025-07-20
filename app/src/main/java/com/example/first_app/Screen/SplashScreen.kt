package com.example.first_app.Screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.first_app.R
import com.example.first_app.ui.theme.splash_background_color
import kotlinx.coroutines.delay

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Splash(alpha: Animatable<Float, AnimationVector1D> = Animatable(0f)){
    Box(
        modifier = Modifier
            .background(splash_background_color)
            .fillMaxSize(),
       contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.splash_icon),
            contentDescription = "Splash Icon",
            modifier = Modifier
                .alpha(alpha.value)
                .clip(RoundedCornerShape(30.dp))
                .size(120.dp)
        )
    }
}