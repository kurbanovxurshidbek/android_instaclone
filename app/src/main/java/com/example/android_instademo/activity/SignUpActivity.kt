package com.example.android_instademo.activity

import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.android_instademo.manager.handler.AuthHandler
import com.example.android_firebase_demo.managers.AuthManager
import com.example.android_instademo.manager.handler.DBUserHandler
import com.example.android_firebase_demo.managers.DatabaseManager
import com.example.android_instademo.R
import com.example.android_instademo.manager.PrefsManager
import com.example.android_instademo.model.User
import com.example.android_instademo.utils.Extensions.toast
import com.example.android_instademo.utils.Logger
import com.example.android_instademo.utils.Utils
import java.lang.Exception

/**
 * In SignUpActivity, user can signup using fullname, email, password
 */
class SignUpActivity : BaseActivity() {
    val TAG = SignUpActivity::class.java.toString()
    lateinit var et_fullname: EditText
    lateinit var et_password: EditText
    lateinit var et_email: EditText
    lateinit var et_cpassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        initViews()
    }

    private fun initViews() {
        et_fullname = findViewById(R.id.et_fullname)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_cpassword = findViewById(R.id.et_cpassword)

        val b_signup = findViewById<Button>(R.id.b_signup)
        b_signup.setOnClickListener {
            val fullname = et_fullname.text.toString().trim()
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            if(fullname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                val user = User(fullname, email, password,"")
                firebaseSignUp(user)
            }
        }
        val tv_signin = findViewById<TextView>(R.id.tv_signin)
        tv_signin.setOnClickListener { finish() }
    }

    private fun firebaseSignUp(user: User) {
        showLoading(this)
        AuthManager.signUp(user.email, user.password, object : AuthHandler {
            override fun onSuccess(uid: String) {
                user.uid = uid
                storeUserToDB(user)
                toast(getString(R.string.str_signup_success))
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast(getString(R.string.str_signup_failed))
            }
        })
    }

    private fun storeUserToDB(user: User){
        user.device_token = PrefsManager(this).loadDeviceToken()!!
        user.device_id = Utils.getDeviceID(this)

        DatabaseManager.storeUser(user, object: DBUserHandler {

            override fun onSuccess(user: User?) {
                dismissLoading()
                callMainActivity(context)
            }

            override fun onError(e: Exception) {

            }
        })
    }
}