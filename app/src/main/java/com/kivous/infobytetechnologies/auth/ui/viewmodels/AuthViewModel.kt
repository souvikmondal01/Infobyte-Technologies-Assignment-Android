package com.kivous.infobytetechnologies.auth.ui.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.kivous.infobytetechnologies.auth.models.User
import com.kivous.infobytetechnologies.auth.repositories.AuthRepository

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var loginListener: LoginListener? = null
    var registerListener: RegisterListener? = null

    fun onRegisterClick(view: View) {
        registerListener?.onStarted()
        if (name.isNullOrBlank()) {
            registerListener?.onEmptyName()
            return
        }
        if (email.isNullOrBlank()) {
            registerListener?.onEmptyEmail()
            return
        }
        if (password.isNullOrBlank()) {
            registerListener?.onEmptyEmail()
            return
        }
        registerListener?.onSuccess()
    }

    fun onLoginTextClick(view: View) {
        registerListener?.onLoginTextClick()
    }

    fun onLoginClick(view: View) {
        loginListener?.onStarted()
        if (email.isNullOrEmpty()) {
            loginListener?.onEmptyEmail()
            return
        }
        if (password.isNullOrEmpty()) {
            loginListener?.onEmptyPassword()
            return
        }
        loginListener?.onSuccess()

    }

    fun onRegisterTextClick(view: View) {
        loginListener?.onRegisterTextClick()
    }

    fun onForgotPassword(view: View) {
        loginListener?.onStarted()
        if (email.isNullOrEmpty()) {
            loginListener?.onEmptyEmail()
            return
        }
        loginListener?.restPassword()
    }

    fun onFacebookClick(view: View) {
        loginListener?.onFacebookClick()
    }

    fun onGoogleClick(view: View) {
        loginListener?.onGoogleClick()
    }

    fun onTwitterClick(view: View) {
        loginListener?.onTwitterClick()
    }

    fun registerUser(result: (String) -> Unit) {
        repository.registerUser(User(null, name, email, password)) {
            result.invoke(it)
        }
    }

    fun loginUser(result: (String) -> Unit) {
        repository.loginUser(User(null, name, email, password)) {
            result.invoke(it)
        }
    }

    fun resetPassword(result: (String) -> Unit) {
        repository.resetPassword(email.toString()) {
            result.invoke(it)
        }
    }

}

interface LoginListener {
    fun onRegisterTextClick()
    fun onFacebookClick()
    fun onGoogleClick()
    fun onTwitterClick()
    fun onStarted()
    fun onSuccess()
    fun onEmptyEmail()
    fun onEmptyPassword()
    fun restPassword()
}

interface RegisterListener {
    fun onLoginTextClick()
    fun onStarted()
    fun onSuccess()
    fun onEmptyName()
    fun onEmptyEmail()
    fun onEmptyPassword()
}