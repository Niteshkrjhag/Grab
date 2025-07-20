package com.example.first_app.Login_auth
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _userEmail = mutableStateOf<String?>(null)
    val userEmail: State<String?> = _userEmail

    init {
        val user = FirebaseAuth.getInstance().currentUser
        _userEmail.value = user?.email
    }
}
