package com.example.first_app.presentation.onboarding

import androidx.annotation.DrawableRes
import com.example.first_app.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)
val pages= listOf(
    Page("Explore the Latest News","Dive into the world of technology, startups, coding competitions," +
            " and more with a single tap. Your home screen brings you the latest stories, curated to keep you ahead in the tech world. "
           , R.drawable.onboarding1),
    Page("Powerful Search at Your Fingertips","Searching for specific news, internships, or coding challenges? " +
            "Our powerful search feature helps you quickly locate what matters most to you. Just type, search,"
            , R.drawable.onboarding2),
    Page("Bookmark,Share and Read, Your Must-Reads","Found something interesting?" +
            " Bookmark articles with a single tap to easily access them later.", R.drawable.onboarding3)
)