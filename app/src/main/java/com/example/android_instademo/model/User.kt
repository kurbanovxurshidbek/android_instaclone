package com.example.android_instademo.model

class User {
    var uid: String = ""
    var fullname: String = ""
    var email: String = ""
    var password: String = ""
    var userImg: String = ""

    var device_id = ""
    var device_type = "A"
    var device_token = ""

    var isFollowed: Boolean = false

    constructor(fullname: String, email: String) {
        this.email = email
        this.fullname = fullname
    }

    constructor(fullname: String, email: String, userImg: String) {
        this.email = email
        this.fullname = fullname
        this.userImg = userImg
    }

    constructor(fullname: String, email: String, password:String, userImg: String) {
        this.email = email
        this.fullname = fullname
        this.userImg = userImg
        this.password = password
    }

}