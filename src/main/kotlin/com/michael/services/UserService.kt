package com.michael.services

import com.michael.entities.User
import com.michael.models.UserDAO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserService: KoinComponent {
    private val userDAO: UserDAO by inject()

    fun getUserByUid(uid: Int): User? {
        return userDAO.getUserByUid(uid)
    }

    fun getUserByEmail(email: String): User? {
        return userDAO.getUserByEmail(email)
    }

    fun newUser(user: User) {
        val defaultPrefix = "user_"
        if(user.username == ""){
            val randomString = getRandomString(8)
            user.username = "${defaultPrefix}${randomString}"
        }
        userDAO.newUser(user)
    }

    private fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

}