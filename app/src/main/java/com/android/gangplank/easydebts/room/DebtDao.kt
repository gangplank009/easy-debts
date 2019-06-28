package com.android.gangplank.easydebts.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.gangplank.easydebts.room.entities.Debt

@Dao
interface DebtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(debt: Debt)

    @Update
    fun update(debt: Debt)

    @Delete
    fun delete(debt: Debt)

    @Query("SELECT * FROM debt_table WHERE debtor_id = :debtorId")
    fun getAllDebtsForDebtor(debtorId: Int): LiveData<List<Debt>>

    @Query("SELECT * FROM debt_table")
    fun getAllDebts(): LiveData<List<Debt>>

    @Query("DELETE FROM debt_table")
    fun deleteAllDebts()
}