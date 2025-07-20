package com.example.first_app.domain.usecase.app_entry

import com.example.first_app.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadAppEntry @Inject constructor(
    private val localUserManager: LocalUserManager
) {
     operator fun invoke(): Flow<Boolean> {
        return localUserManager.readAppEntry()
    }
}