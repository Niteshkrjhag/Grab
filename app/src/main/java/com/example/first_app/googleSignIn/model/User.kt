package com.example.first_app.googleSignIn.model

data class User(
    var id: String = "",
    val name: String = "",
    val photoUrl: String = "",
    val email: String = "",
    val country: String = "",
    val subscription: String = ""
)