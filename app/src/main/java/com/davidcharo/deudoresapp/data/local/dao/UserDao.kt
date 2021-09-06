package com.davidcharo.deudoresapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.davidcharo.deudoresapp.data.local.entities.User


@Dao
interface UserDao {

    @Insert
    fun createUser(user: User)

    @Query("SELECT * FROM table_user WHERE email LIKE :email")
    fun readUser(email: String) : User
}