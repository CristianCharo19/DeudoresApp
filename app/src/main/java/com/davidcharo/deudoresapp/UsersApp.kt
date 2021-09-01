package com.davidcharo.deudoresapp

import android.app.Application
import androidx.room.Room
import com.davidcharo.deudoresapp.data.UserDatabase

class UsersApp : Application() {

    companion object{
        lateinit var databaseUser: UserDatabase
    }

    override fun onCreate() {
        super.onCreate()

        databaseUser = Room.databaseBuilder(
            this,
            UserDatabase::class.java,
            "user_db"
        ).allowMainThreadQueries()
            .build()
    }
}