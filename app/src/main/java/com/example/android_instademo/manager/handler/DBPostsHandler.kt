package com.example.android_instademo.manager.handler

import com.example.android_instademo.model.Post
import com.example.android_instademo.model.User
import java.lang.Exception

interface DBPostsHandler {
    fun onSuccess(posts: ArrayList<Post>)
    fun onError(e: Exception)
}