package com.example.android_instademo.manager.handler

import com.example.android_instademo.model.Post
import java.lang.Exception

interface DBFollowHandler {
    fun onSuccess(isDone: Boolean)
    fun onError(e: Exception)
}