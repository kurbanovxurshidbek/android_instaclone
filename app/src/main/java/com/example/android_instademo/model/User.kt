package com.example.android_instademo.model

class User {
    var uid: String = ""
    var fullname: String = ""
    var email: String = ""
    var password: String = ""
    var image: String = ""

    constructor(fullname: String, email: String) {
        this.email = email
        this.fullname = fullname
    }

    constructor(fullname: String, email: String, image: String) {
        this.email = email
        this.fullname = fullname
        this.image = image
    }

    constructor(fullname: String, email: String, password:String, image: String) {
        this.email = email
        this.fullname = fullname
        this.image = image
        this.password = password
    }

}