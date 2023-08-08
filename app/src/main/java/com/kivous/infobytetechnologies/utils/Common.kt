package com.kivous.infobytetechnologies.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kivous.infobytetechnologies.R

object Common {
    val auth = Firebase.auth

    @SuppressLint("StaticFieldLeak")
    val db = Firebase.firestore

    /**
    Show password on visibility icon click
     */
    fun showPassword(isShow: Boolean, et: EditText, iv: ImageView) {
        if (isShow) {
            et.transformationMethod = HideReturnsTransformationMethod.getInstance()
            iv.setImageResource(R.drawable.visibility)
        } else {
            et.transformationMethod = PasswordTransformationMethod.getInstance()
            iv.setImageResource(R.drawable.visibility_off)
        }
        //to change cursor position
        et.setSelection(et.text.toString().length)
    }

    /**
    show cancel icon when edittext is not empty and clear edittext on cancel icon click
     */
    fun clearEdittext(et: EditText, iv: ImageView) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et.text.toString().isEmpty()) {
                    iv.visibility = View.GONE
                } else {
                    iv.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                iv.setOnClickListener {
                    et.text.clear()
                }
            }
        })
    }

    /**
    Change icon color when edittext is not in focus
     */
    fun changeIconColorWhenEdittextNotInFocus(context: Context, et: EditText, iv: ImageView) {
        et.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                iv.setColorFilter(Color.GRAY)
            } else {
                iv.setColorFilter(
                    ContextCompat.getColor(
                        context, R.color.blue
                    )
                )
            }
        }
    }
}