package com.example.android_instademo.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.android_instademo.manager.handler.AuthHandler
import com.example.android_firebase_demo.managers.AuthManager
import com.example.android_instademo.R
import com.example.android_instademo.manager.FirebaseConfig
import com.example.android_instademo.manager.PrefsManager
import com.example.android_instademo.utils.DeepLink
import com.example.android_instademo.utils.Extensions.toast
import com.example.android_instademo.utils.Logger
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import java.lang.Exception

/**
 * In SignUpActivity, user can login using email, password
 */
class SignInActivity : BaseActivity() {
    val TAG = SignInActivity::class.java.toString()
    lateinit var et_email: EditText
    lateinit var et_password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        initViews()
    }

    private fun initViews() {
        val ll_background = findViewById<LinearLayout>(R.id.ll_background)
        val tv_welcome = findViewById<TextView>(R.id.tv_welcome)

        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        val b_signin = findViewById<Button>(R.id.b_signin)
        b_signin.setOnClickListener {
            //FirebaseConfig(ll_background, tv_welcome).updateConfig()
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
//            if (email.isNotEmpty() && password.isNotEmpty())
//                firebaseSignIn(email, password)
            val str1 = reverse(email)
            val str2 = reverse(password)
        }
        val tv_signup = findViewById<TextView>(R.id.tv_signup)
        tv_signup.setOnClickListener { callSignUpActivity() }

        val tv_link = findViewById<TextView>(R.id.tv_link)
        //DeepLink.retrieveLink(intent,tv_link)
        FirebaseConfig(ll_background, tv_welcome).applyConfig()
    }

    private fun firebaseSignIn(email: String, password: String) {
        showLoading(this)
        AuthManager.signIn(email, password, object : AuthHandler {
            override fun onSuccess(uid: String) {
                dismissLoading()
                toast(getString(R.string.str_signin_success))
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast(getString(R.string.str_signin_failed))
            }
        })
    }

    private fun callSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }


    fun reverse(str: String): String {
        val chars: CharArray = str.toCharArray()

        var l = 0
        var h = str.length - 1
        while (l < h) {
            val c = chars[l]
            chars[l] = chars[h]
            chars[h] = c
            l++; h--;
        }

        return String(chars)
    }

}