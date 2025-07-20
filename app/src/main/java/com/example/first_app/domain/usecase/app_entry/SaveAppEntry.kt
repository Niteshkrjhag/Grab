package com.example.first_app.domain.usecase.app_entry

import com.example.first_app.domain.manager.LocalUserManager
import javax.inject.Inject

class SaveAppEntry@Inject constructor(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){ // operator is used to call the function by class name
        localUserManager.saveAppEntry()
    }
}