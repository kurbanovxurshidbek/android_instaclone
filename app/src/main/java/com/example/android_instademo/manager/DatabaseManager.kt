package com.example.android_firebase_demo.managers

import com.example.android_instademo.manager.handler.*
import com.example.android_instademo.model.Post
import com.example.android_instademo.model.User
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

private var USER_PATH = "users"
private var POST_PATH = "posts"
private var FEED_PATH = "feeds"
private var FOLLOWING_PATH = "following"
private var FOLLOWERS_PATH = "followers"

object DatabaseManager {
    private var database = FirebaseFirestore.getInstance()

    fun storePostsToMyFeed(uid: String, to: User) {
        loadPosts(to.uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                for (post in posts) {
                    storeFeed(uid, post)
                }
            }

            override fun onError(e: Exception) {

            }
        })
    }

    fun storeFeed(uid: String, post: Post) {
        val reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH)
        reference.document(post.id).set(post)
    }

    fun removePostsFromMyFeed(uid: String, to: User) {
        loadPosts(to.uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                for (post in posts) {
                    removeFeed(uid, post)
                }
            }

            override fun onError(e: Exception) {

            }
        })
    }

    fun removeFeed(uid: String, post: Post) {
        val reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH)
        reference.document(post.id).delete()
    }

    fun followUser(me: User, to: User, handler: DBFollowHandler) {
        // User(To) is in my following
        database.collection(USER_PATH).document(me.uid).collection(FOLLOWING_PATH).document(to.uid)
            .set(to).addOnSuccessListener {
                // User(Me) is in his/her followers
                database.collection(USER_PATH).document(to.uid).collection(FOLLOWERS_PATH)
                    .document(me.uid)
                    .set(me).addOnSuccessListener {
                        handler.onSuccess(true)
                    }.addOnFailureListener {
                        handler.onError(it)
                    }
            }.addOnFailureListener {
                handler.onError(it)
            }
    }

    fun unFollowUser(me: User, to: User, handler: DBFollowHandler) {
        // User(To) is in my following
        database.collection(USER_PATH).document(me.uid).collection(FOLLOWING_PATH).document(to.uid)
            .delete().addOnSuccessListener {
                // User(Me) is in his/her followers
                database.collection(USER_PATH).document(to.uid).collection(FOLLOWERS_PATH)
                    .document(me.uid)
                    .delete().addOnSuccessListener {
                        handler.onSuccess(true)
                    }.addOnFailureListener {
                        handler.onError(it)
                    }
            }.addOnFailureListener {
                handler.onError(it)
            }
    }

    fun loadFollowing(uid: String, handler: DBUsersHandler) {
        database.collection(USER_PATH).document(uid).collection(FOLLOWING_PATH).get()
            .addOnCompleteListener {
                val users = ArrayList<User>()
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        val uid = document.getString("uid")
                        val fullname = document.getString("fullname")
                        val email = document.getString("email")
                        val userImg = document.getString("userImg")
                        val user = User(fullname!!, email!!, userImg!!)
                        user.uid = uid!!
                        users.add(user)
                    }
                    handler.onSuccess(users)
                } else {
                    handler.onError(it.exception!!)
                }
            }
    }

    fun loadFollowers(uid: String, handler: DBUsersHandler) {
        database.collection(USER_PATH).document(uid).collection(FOLLOWERS_PATH).get()
            .addOnCompleteListener {
                val users = ArrayList<User>()
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        val uid = document.getString("uid")
                        val fullname = document.getString("fullname")
                        val email = document.getString("email")
                        val userImg = document.getString("userImg")
                        val user = User(fullname!!, email!!, userImg!!)
                        user.uid = uid!!
                        users.add(user)
                    }
                    handler.onSuccess(users)
                } else {
                    handler.onError(it.exception!!)
                }
            }
    }

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
                val userImg: String? = it.getString("userImg")

                val user = User(fullname!!, email!!, userImg!!)
                user.uid = uid
                handler.onSuccess(user)
            } else {
                handler.onSuccess(null)
            }
        }.addOnFailureListener {
            handler.onError(it)
        }
    }

    fun updateUserImage(userImg: String) {
        val uid = AuthManager.currentUser()!!.uid
        database.collection(USER_PATH).document(uid).update("userImg", userImg)
    }

    fun loadUsers(handler: DBUsersHandler) {
        database.collection(USER_PATH).get().addOnCompleteListener {
            val users = ArrayList<User>()
            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val uid = document.getString("uid")
                    val fullname = document.getString("fullname")
                    val email = document.getString("email")
                    val userImg = document.getString("userImg")
                    var device_id = document.getString("device_id")
                    var device_type = document.getString("device_type")
                    var device_token = document.getString("device_token")
                    if (device_id == null) device_id = ""
                    if (device_type == null) device_type = ""
                    if (device_token == null) device_token = ""

                    val user = User(fullname!!, email!!, userImg!!)
                    user.uid = uid!!
                    user.device_id = device_id
                    user.device_type = device_type
                    user.device_token = device_token
                    users.add(user)
                }
                handler.onSuccess(users)
            } else {
                handler.onError(it.exception!!)
            }
        }
    }

    fun storePosts(post: Post, handler: DBPostHandler) {
        val reference = database.collection(USER_PATH).document(post.uid).collection(POST_PATH)
        val id = reference.document().id
        post.id = id

        reference.document(post.id).set(post).addOnSuccessListener {
            handler.onSuccess(post)
        }.addOnFailureListener {
            handler.onError(it)
        }
    }

    fun storeFeeds(post: Post, handler: DBPostHandler) {
        val reference = database.collection(USER_PATH).document(post.uid).collection(FEED_PATH)

        reference.document(post.id).set(post).addOnSuccessListener {
            handler.onSuccess(post)
        }.addOnFailureListener {
            handler.onError(it)
        }
    }

    fun loadPosts(uid: String, handler: DBPostsHandler) {
        val reference = database.collection(USER_PATH).document(uid).collection(POST_PATH)
        reference.get().addOnCompleteListener {
            val posts = ArrayList<Post>()
            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val id = document.getString("id")
                    val caption = document.getString("caption")
                    val postImg = document.getString("postImg")
                    val fullname = document.getString("fullname")
                    val userImg = document.getString("userImg")
                    val currentDate = document.getString("currentDate")
                    var isLiked = document.getBoolean("isLiked")
                    if (isLiked == null) isLiked = false

                    val post = Post(id!!, caption!!, postImg!!)
                    post.uid = uid
                    post.fullname = fullname!!
                    post.userImg = userImg!!
                    post.currentDate = currentDate!!
                    post.isLiked = isLiked!!
                    posts.add(post)
                }
                handler.onSuccess(posts)
            } else {
                handler.onError(it.exception!!)
            }
        }
    }

    fun loadFeeds(uid: String, handler: DBPostsHandler) {
        val reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH)
        reference.get().addOnCompleteListener {
            val posts = ArrayList<Post>()
            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val id = document.getString("id")
                    val caption = document.getString("caption")
                    val postImg = document.getString("postImg")
                    val fullname = document.getString("fullname")
                    val userImg = document.getString("userImg")
                    val currentDate = document.getString("currentDate")
                    var isLiked = document.getBoolean("isLiked")
                    if (isLiked == null) isLiked = false
                    val userId = document.getString("uid")

                    val post = Post(id!!, caption!!, postImg!!)
                    post.uid = userId!!
                    post.fullname = fullname!!
                    post.userImg = userImg!!
                    post.currentDate = currentDate!!
                    post.isLiked = isLiked
                    posts.add(post)
                }
                handler.onSuccess(posts)
            } else {
                handler.onError(it.exception!!)
            }
        }
    }

    fun likeFeedPost(uid: String, post: Post) {
        database.collection(USER_PATH).document(uid).collection(FEED_PATH).document(post.id)
            .update("isLiked", post.isLiked)
        if (uid == post.uid)
            database.collection(USER_PATH).document(uid).collection(POST_PATH).document(post.id)
                .update("isLiked", post.isLiked)
    }

    fun loadLikedFeeds(uid: String, handler: DBPostsHandler) {
        val reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH)
            .whereEqualTo("isLiked", true)
        reference.get().addOnCompleteListener {
            val posts = ArrayList<Post>()
            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val id = document.getString("id")
                    val caption = document.getString("caption")
                    val postImg = document.getString("postImg")
                    val fullname = document.getString("fullname")
                    val userImg = document.getString("userImg")
                    val currentDate = document.getString("currentDate")
                    var isLiked = document.getBoolean("isLiked")
                    if (isLiked == null) isLiked = false
                    val userId = document.getString("uid")

                    val post = Post(id!!, caption!!, postImg!!)
                    post.uid = userId!!
                    post.fullname = fullname!!
                    post.userImg = userImg!!
                    post.currentDate = currentDate!!
                    post.isLiked = isLiked
                    posts.add(post)
                }
                handler.onSuccess(posts)
            } else {
                handler.onError(it.exception!!)
            }
        }
    }

    fun deletePost(post: Post, handler: DBPostHandler) {
        val reference1 = database.collection(USER_PATH).document(post.uid).collection(POST_PATH)
        reference1.document(post.id).delete().addOnSuccessListener {

            val reference2 = database.collection(USER_PATH).document(post.uid).collection(FEED_PATH)
            reference2.document(post.id).delete().addOnSuccessListener {
                handler.onSuccess(post)
            }.addOnFailureListener {
                handler.onError(it)
            }

        }.addOnFailureListener {
            handler.onError(it)
        }
    }
}