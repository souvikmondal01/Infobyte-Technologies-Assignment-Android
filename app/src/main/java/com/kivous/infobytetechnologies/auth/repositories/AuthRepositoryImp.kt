package com.kivous.infobytetechnologies.auth.repositories

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kivous.infobytetechnologies.auth.models.User
import com.kivous.infobytetechnologies.utils.Common.auth
import com.kivous.infobytetechnologies.utils.Common.db
import com.kivous.infobytetechnologies.utils.Constant.EMAIL_NOT_VERIFIED_MSG
import com.kivous.infobytetechnologies.utils.Constant.LOGIN_SUCCESS
import com.kivous.infobytetechnologies.utils.Constant.REGISTRATION_SUCCESS_MSG
import com.kivous.infobytetechnologies.utils.Constant.RESET_PASSWORD_MSG
import com.kivous.infobytetechnologies.utils.Constant.TAG
import com.kivous.infobytetechnologies.utils.Constant.USER
import com.kivous.infobytetechnologies.utils.Constant.USER_SET_MSG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImp : AuthRepository {

    override fun registerUser(user: User, result: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(
                    user.email.toString(),
                    user.password.toString()
                ).await()
                user.id = authResult.user!!.uid
                authResult.user!!.sendEmailVerification().await()
                setUser(user) {
                    Log.d(TAG, it)
                }
                withContext(Dispatchers.Main) {
                    result.invoke(REGISTRATION_SUCCESS_MSG)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.invoke(e.message.toString())
                }
            }
        }
    }

    override fun setUser(user: User?, result: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                user?.let {
                    db.collection(USER).document(user.id.toString()).set(it).await()
                    withContext(Dispatchers.Main) {
                        result.invoke(USER_SET_MSG)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.invoke(e.message.toString())
                }
            }
        }
    }

    override fun loginUser(user: User, result: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val authResult =
                    auth.signInWithEmailAndPassword(
                        user.email.toString(),
                        user.password.toString()
                    ).await()
                withContext(Dispatchers.Main) {
                    if (authResult.user!!.isEmailVerified) {
                        result.invoke(LOGIN_SUCCESS)
                        return@withContext
                    }
                    result.invoke(EMAIL_NOT_VERIFIED_MSG)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.invoke(
                        e.message.toString()
                    )
                }
            }
        }
    }

    override fun resetPassword(email: String?, result: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.sendPasswordResetEmail(email.toString()).await()
                withContext(Dispatchers.Main) {
                    result.invoke(RESET_PASSWORD_MSG)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.invoke(e.message.toString())
                }
            }
        }
    }
}