package com.davidcharo.deudoresapp

import android.app.Application
import androidx.room.Room
import com.davidcharo.deudoresapp.data.DebtorDatabase
import com.davidcharo.deudoresapp.data.UserDatabase

class DeudoresApp : Application() {

    companion object{
        lateinit var database: DebtorDatabase
        lateinit var databaseUser: UserDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            this,
            DebtorDatabase::class.java,
            "debtor_db"
        ).allowMainThreadQueries()
            .build()

        databaseUser = Room.databaseBuilder(
            this,
            UserDatabase::class.java,
            "user_db"
        ).allowMainThreadQueries()
            .build()
    }
}