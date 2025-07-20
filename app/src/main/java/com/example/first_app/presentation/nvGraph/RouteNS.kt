package com.example.first_app.presentation.nvGraph


// NS -> news screen
sealed class RouteNS(
    val route: String
) {
    object  OnBoardingScreen :RouteNS(route = "onBoardingScreen")
    object  HomeScreen :RouteNS(route = "homeScreen")
    object  SearchScreen :RouteNS(route = "searchScreen")
    object  BookmarkScreen :RouteNS(route = "bookmarkScreen")
    object  DetailsScreen :RouteNS(route = "detailsScreen")
    object  AppStartNavigation :RouteNS(route = "appStartNavigation")
    object  NewsNavigation :RouteNS(route = "newsNavigation")
    object  NewsNavigatorScreen :RouteNS(route = "newsNavigator")
    object  SettingsScreen: RouteNS(route = "settingsScreen")
    object  ChatBotScreen: RouteNS(route = "chatBotScreen")
    object  ChatBotNavigation: RouteNS(route = "chatBotNavigation")
}