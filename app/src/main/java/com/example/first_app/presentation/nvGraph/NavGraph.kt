package com.example.first_app.presentation.nvGraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
//import com.example.first_app.ChatBot.ChatViewModel.ChatViewModel
//import com.example.first_app.ChatBot.ScreenCB.ChatBotScreen
import com.example.first_app.navigation.Routes
import com.example.first_app.presentation.news_navigator.NewsNavigator
import com.example.first_app.presentation.onboarding.OnBoardingScreen
import com.example.first_app.presentation.onboarding.OnBoardingViewModel

@Preview
@Composable
fun NavGraph(
    StartDestination : String
) {
    val navController = rememberNavController()
    NavHost(navController=navController,startDestination = StartDestination){
         navigation(
             route = RouteNS.AppStartNavigation.route,
             startDestination = RouteNS.OnBoardingScreen.route
         ){
             composable(
                 route = RouteNS.OnBoardingScreen.route
             ){
                 val viewModel: OnBoardingViewModel = hiltViewModel()
                 OnBoardingScreen(
                     OnEvent= viewModel::onEvent,navController
                 )
             }
         }

        navigation(
            route = RouteNS.NewsNavigation.route,
            startDestination = RouteNS.NewsNavigatorScreen.route
        ){
            composable(route = RouteNS.NewsNavigatorScreen.route){
                NewsNavigator()
            }
        }



    }
}

