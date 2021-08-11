package com.davidcharo.deudoresapp.data.dao

import androidx.room.*
import com.davidcharo.deudoresapp.data.entities.Debtor


@Dao
interface DebtorDao {

    @Insert
    fun createDebtor(debtor: Debtor)

    @Query("SELECT * FROM table_debtor")
    fun getDebtors() : MutableList<Debtor>

    @Query("SELECT * FROM table_debtor WHERE name LIKE :name")
    fun readDebtor(name: String) : Debtor

    @Delete
    fun deleteDebtor(debtor: Debtor)

    @Update
    fun updateDebtor(debtor: Debtor)

}