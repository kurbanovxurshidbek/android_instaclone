package com.example.android_firebase_demo.managers

import com.example.android_instademo.manager.handler.DBUserHandler
import com.example.android_instademo.manager.handler.DBUsersHandler
import com.example.android_instademo.model.User
import com.google.firebase.firestore.FirebaseFirestore


object DatabaseManager {
    private var database = FirebaseFirestore.getInstance()

    fun storeUser(user: User, handler: DBUserHandler) {
        database.collection(USER_PATH).document(user.uid).set(user).addOnSuccessListener {
            handler.onSuccess()
        }.addOnFailureListener {
            handler.onError(it)
        }
    }

    fun loadUser(uid: String, handler: DBUserHandler) {
        database.collection(USER_PATH).document(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val fullname: String? = it.getString("fullname")
                val email: String? = it.getString("email")
                val image: String? = it.getString("image")

                val user = User(fullname!!, email!!, image!!)
                user.uid = uid
                handler.onSuccess(user)
            } else {
                handler.onSuccess(null)
            }
        }.addOnFailureListener {
            handler.onError(it)
        }
    }

    fun updateUserImage(image: String) {
        val uid = AuthManager.currentUser().uid
        database.collection(USER_PATH).document(uid).update("image", image)
    }

    fun loadUsers(handler: DBUsersHandler) {
        database.collection(USER_PATH).get().addOnCompleteListener {
            val users = ArrayList<User>()
            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val uid = document.getString("uid")
                    val fullname = document.getString("fullname")
                    val email = document.getString("email")
                    val image = document.getString("image")
                    val user = User(fullname!!, email!!, image!!)
                    user.uid = uid!!
                    users.add(user)
                }
                handler.onSuccess(users)
            } else {
                handler.onError(it.exception!!)
            }
        }
    }

    private var USER_PATH = "users"
    private var POST_PATH = "posts"
    private var FEED_PATH = "feeds"
    private var FOLLOWING_PATH = "following"
    private var FOLLOWERS_PATH = "followers"
}