package com.michael.models

import com.michael.entities.User
import com.michael.entities.Users
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.limit
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.where

class UserDAO : KoinComponent {
    private val database: Database by inject()
    fun getUserByUid(uid: Int): User? {
        val user = database.from(Users).select().where {
            Users.uid eq uid
        }.limit(1).map {
            Users.createEntity(it)
        }
        return if (user.isNotEmpty()) {
            user[0]
        } else {
            null
        }
    }

    fun getUserByEmail(email: String): User? {
        val user = database.from(Users).select().where {
            Users.email eq email
        }.limit(1).map { Users.createEntity(it) }
        if (user.isNotEmpty()) {
            return user[0]
        }
        return null
    }

    fun newUser(user: User) {
        val users = database.from(Users).select().where {
            Users.email eq user.email
        }.map { Users.createEntity(it) }
        if (users.isNotEmpty()) {
            throw Exception("user already exists")
        }
        database.insert(Users) {
            set(it.email, user.email)
            set(it.username, user.username)
            set(it.password, user.password)
        }
    }
}
