package com.example.genst.data.preference

data class UserModel(
    val id: Int,
    val token: String,
    val email: String,
    val badgeNumber: String,
    val isLogin: Boolean = false
)