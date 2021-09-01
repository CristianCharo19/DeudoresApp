package com.davidcharo.deudoresapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidcharo.deudoresapp.data.dao.UserDao
import com.davidcharo.deudoresapp.data.entities.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun UserDao(): UserDao
}