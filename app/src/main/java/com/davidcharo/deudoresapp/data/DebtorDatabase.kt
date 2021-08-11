package com.davidcharo.deudoresapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidcharo.deudoresapp.data.dao.DebtorDao
import com.davidcharo.deudoresapp.data.entities.Debtor

@Database(entities = [Debtor::class], version = 1)
abstract class DebtorDatabase: RoomDatabase() {

    abstract fun DebtorDao(): DebtorDao


}