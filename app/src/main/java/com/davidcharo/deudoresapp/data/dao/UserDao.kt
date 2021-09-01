package com.davidcharo.deudoresapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.davidcharo.deudoresapp.data.entities.Debtor
import com.davidcharo.deudoresapp.data.entities.User


@Dao
interface UserDao {

    @Insert
    fun createUser(user: User)

    @Query("SELECT * FROM table_user WHERE name LIKE :name")
    fun readUser(name: String) : User
}