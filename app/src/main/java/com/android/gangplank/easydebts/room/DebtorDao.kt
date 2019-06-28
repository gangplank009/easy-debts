package com.android.gangplank.easydebts.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.gangplank.easydebts.room.entities.Debtor

@Dao
interface DebtorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(debtor: Debtor)

    @Update
    fun update(debtor: Debtor)

    @Delete
    fun delete(debtor: Debtor)

    @Query("SELECT * FROM debtor_table")
    fun getAllDebtors(): LiveData<List<Debtor>>

    @Query("DELETE FROM debtor_table")
    fun deleteAllDebtors()
}
