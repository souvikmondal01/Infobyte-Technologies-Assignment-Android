package com.kivous.infobytetechnologies.auth.repositories

import com.kivous.infobytetechnologies.auth.models.User

interface AuthRepository {
    fun registerUser(user: User, result: (String) -> Unit)
    fun setUser(user: User?,result: (String) -> Unit)
    fun loginUser(user: User, result: (String) -> Unit)
    fun resetPassword(email: String?, result: (String) -> Unit)
}