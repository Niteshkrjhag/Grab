package com.example.first_app.presentation.onboarding

sealed class OnBoardingEvent {
    object SaveAppEntry : OnBoardingEvent()
}