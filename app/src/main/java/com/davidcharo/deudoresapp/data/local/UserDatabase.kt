package com.davidcharo.deudoresapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidcharo.deudoresapp.data.local.dao.UserDao
import com.davidcharo.deudoresapp.data.local.entities.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun UserDao(): UserDao
}