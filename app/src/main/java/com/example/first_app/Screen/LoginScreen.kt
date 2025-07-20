package com.example.first_app.Screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.first_app.Login_auth.AuthState
import com.example.first_app.Login_auth.LoginViewModel
import com.example.first_app.R
import com.example.first_app.googleSignIn.GoogleSignInViewModel
import com.example.first_app.navigation.Routes
import com.example.first_app.navigation.Routes.BoardingScreen
import com.example.first_app.ui.theme.Teal200
import com.example.first_app.ui.theme.splash_background_color


@Composable
fun Login(navController: NavHostController,
          authViewModel: LoginViewModel,
          googleSignInViewModel: GoogleSignInViewModel) {
Log.d("SignOut","Inside login screen")
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current


    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate(BoardingScreen)
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize() // Center Column horizontally
            .background(Color.White)
    ) {
        val login_pic = painterResource(id = R.drawable.login_photo)
        Image(
            painter = login_pic,
            contentDescription = "Login Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Fit,
        )
        Text(
            text = "Welcome Back",
            fontSize = 30.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Login to your account",
            color = Color.Gray,
            fontWeight = FontWeight(100),
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .align(Alignment.CenterHorizontally)
        )

        var email_add by remember { mutableStateOf("") }
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email_add,
            onValueChange = {
                email_add = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next)
            ,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .align(Alignment.CenterHorizontally),
                colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,// Set the container color to white
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.Green,
                focusedTextColor = Color.Black,

            ),
            label = {
                Text(text = "Email Address",
                    color = Color.Gray,
                    fontWeight = FontWeight(170),
                )
            },
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        var password by remember { mutableStateOf("") }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {password = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .align(Alignment.CenterHorizontally),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,// Set the container color to white
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedTextColor = Color.Black
            ),
            label = {
                Text(text = "Password", color = Color.Gray, fontWeight = FontWeight(170))
            },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Forgot Password?",
            fontSize = 10.sp,
            color = Color.Gray,
            fontWeight = FontWeight(250),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(top = 3.dp, end = 20.dp)
                .wrapContentWidth(Alignment.End)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                    Log.d("LS_btn","Clicked")
                authViewModel.login(email_add,password)
            },
            enabled = authState.value != AuthState.Loading
            ,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Or sign in with",
            fontSize = 10.sp,
            color = Color.Gray,
            fontWeight = FontWeight(250),
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val google_login = painterResource(id = R.drawable.google_login)


            ClickableImage(
               google_login = google_login,
               onClick = {
                  googleSignInViewModel.handleGoogleSignIn(context, navController)
                  Log.d("click", "Google Sign-In Clicked")
               }
            )
//            Spacer(modifier = Modifier.width(20.dp))
////            ClickableImage(google_login = facebook_login,
////                onClick = {}
////                )
//            Spacer(modifier = Modifier.width(20.dp))
////            ClickableImage(google_login = twitter_login,
////                onClick = {
////
////                }
////            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        val annotatedText = buildAnnotatedString {
            append("Don't Have an Account? ")
            pushStringAnnotation(tag = "Sign Up", annotation = Routes.SignUp)
            withStyle(style = SpanStyle(color = Color.Blue, fontWeight =FontWeight.Bold)) {
                append(" Sign Up")
            }
            pop()
        }
        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations("Sign Up", offset, offset)
                    .firstOrNull()?.let { annotation ->
                        navController.navigate(annotation.item){
                            popUpTo(Routes.Login) { inclusive = true}
                        }
                    }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClickableImage(
    google_login: Painter,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(45.dp)
            .clip(CircleShape)
            .clickable(
               onClick = { onClick() },
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
                            Teal200,
                            splash_background_color
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
