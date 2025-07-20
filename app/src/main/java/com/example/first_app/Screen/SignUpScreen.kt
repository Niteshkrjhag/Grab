package com.example.first_app.Screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.first_app.Login_auth.AuthState
import com.example.first_app.Login_auth.LoginViewModel
import com.example.first_app.R
import com.example.first_app.googleSignIn.GoogleSignInViewModel
import com.example.first_app.navigation.Routes
import com.example.first_app.navigation.Routes.BoardingScreen
import com.example.first_app.presentation.nvGraph.RouteNS
import com.example.first_app.presentation.onboarding.OnBoardingScreen
import com.example.first_app.ui.theme.Purple200
import com.example.first_app.ui.theme.PurpleGrey80
import com.example.first_app.ui.theme.Teal200
import com.example.first_app.ui.theme.splash_background_color


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun signUpScreen(
    navController: NavHostController,
    authViewModel: LoginViewModel,
    googleSignInViewModel: GoogleSignInViewModel
) {

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current


    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate(BoardingScreen)
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message,Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    val blue = colorResource(id = R.color.purple_700).copy(alpha = 0.5f)
    val signUp_background_image = painterResource(id = R.drawable.signup_background_image)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(blue),
        contentAlignment = Alignment.TopStart
    ) {
        val semiTransparentRed = colorResource(id = R.color.purple_700).copy(alpha = 0.5f)
        Image(
            painter = signUp_background_image,
            contentDescription = "Sign Up Background",
            modifier = Modifier
                .padding(top = 0.dp)
                .fillMaxWidth(),
            colorFilter = ColorFilter.tint(semiTransparentRed, blendMode = BlendMode.Hardlight),
            contentScale = ContentScale.Fit
        )
        Text(
            AnnotatedString(text = "Create an Account "),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp),
            textAlign = TextAlign.Center,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.white),
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 225.dp, start = 5.dp, end = 5.dp)
                .fillMaxHeight(),
            colors = CardDefaults.cardColors(containerColor =Color.White),
            shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
        ) {
            Text(
                text = "Sign Up with linked account",
                fontSize = 10.sp,
                fontWeight = FontWeight(250),
                modifier = Modifier
                    .padding(top = 25.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = Modifier
                    .padding(13.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center,

            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(45.dp)
                        .clip(CircleShape)
                        .clickable(
                            onClick = { googleSignInViewModel.handleGoogleSignIn(context, navController) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = Color.Gray)
                        )
                        .wrapContentSize()
                ){
                    Row(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        PurpleGrey80,
                                        Purple200

                                    ),
                                )
                            )
                            .fillMaxSize()
                            .padding(10.dp),
                        // contentAlignment = Alignment.Center
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.google_login),
                            contentDescription = "Google"
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = "Continue with Google"
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // Align items vertically
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f) // Occupy available space on the left
                        .height(1.dp) // Adjust line thickness as needed
                        .background(Color.Gray) // Adjust line color as needed
                )
                Text(
                    text = " OR ", // Add spacing around the text
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 13.sp,
                    color = Color.Black.copy(alpha = 0.6f)
                )
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f) // Occupy available space on the right
                        .height(1.dp)
                        .background(Color.Gray)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var email by remember { mutableStateOf("") }
                var isFocused by remember { mutableStateOf(false) }
                val labelFontSize = if (email.isEmpty() && !isFocused) 13.sp else 8.sp

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .align(Alignment.CenterHorizontally)
                        .onFocusChanged { focusState -> isFocused = focusState.isFocused },
                    label = {
                        Text(
                            text = "Email",
                            color = Color.Gray,
                            fontWeight = FontWeight(200),
                            fontSize = labelFontSize
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                var C_password by remember { mutableStateOf("") }
                var isFocused_password by remember { mutableStateOf(false) }
                val labelFontSize_password = if (C_password.isEmpty() && !isFocused_password) 13.sp else 8.sp
                OutlinedTextField(
                    value = C_password,
                    onValueChange = {C_password=it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),

                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .onFocusChanged { focusState -> isFocused_password = focusState.isFocused }
                        .align(Alignment.CenterHorizontally),
                    label = {
                        Text(text = "Choose Password", color = Color.Gray, fontWeight = FontWeight(200), fontSize = labelFontSize_password)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                var confirm_password by remember { mutableStateOf("") }
                var isFocused_confirm by remember { mutableStateOf(false) }
                val labelFontSize_confirm = if (confirm_password.isEmpty() && !isFocused_confirm) 13.sp else 8.sp
                OutlinedTextField(
                    value = confirm_password,
                    onValueChange = { confirm_password=it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .onFocusChanged { focusState -> isFocused_confirm = focusState.isFocused }
                        .align(Alignment.CenterHorizontally),
                    label = {
                        Text(text = "Confirm Password", color = Color.Gray,
                            fontWeight = FontWeight(200),
                            fontSize =labelFontSize_confirm)
                    },
                    visualTransformation = PasswordVisualTransformation()
                    ,
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(25.dp))
                Button(
                    onClick = {
                        authViewModel.SignUp(email,confirm_password)
                    },
                    enabled = authState.value != AuthState.Loading,
                    modifier = Modifier
                        .fillMaxWidth(0.80f),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(text = "Create account", color = Color.White, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(15.dp))
                val annotatedText = buildAnnotatedString {
                    append("Already have an account? ")
                    pushStringAnnotation(tag = "Sign in", annotation = Routes.Login)
                    withStyle(style = SpanStyle(color = Color.Blue, fontWeight =FontWeight.Bold)) {
                        append(" Sign in")
                    }
                    pop()
                }
                ClickableText(
                    text = annotatedText,
                    onClick = { offset ->
                        annotatedText.getStringAnnotations("Sign in", offset, offset)
                            .firstOrNull()?.let { annotation ->
                                navController.navigate(annotation.item){
                                    popUpTo(Routes.SignUp) { inclusive = true}
                                }
                            }
                    }
                )
                BackHandler {
                    if (navController.currentDestination?.route != Routes.Login) {
                        navController.navigate(Routes.Login) {
                            popUpTo(Routes.SignUp) { inclusive = true }
                        }
                    }
                }
            }
        }
    }

}