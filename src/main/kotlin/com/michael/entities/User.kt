package com.michael.entities

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Users : BaseTable<User>("users") {
    val uid = int("uid").primaryKey()
    val username = varchar("username")
    var email = varchar("email")
    val password = varchar("password")
    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): User {
        return User(row[uid] ?: 0, row[email] ?: "", row[username] ?: "", row[password] ?: "")
    }
}

data class User(val uid: Int?, val email: String, var username: String?, var password: String)
