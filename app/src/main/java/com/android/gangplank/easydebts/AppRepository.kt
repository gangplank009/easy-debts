package com.android.gangplank.easydebts

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android.gangplank.easydebts.room.AppDatabase
import com.android.gangplank.easydebts.room.DebtDao
import com.android.gangplank.easydebts.room.DebtorDao
import com.android.gangplank.easydebts.room.entities.Debt
import com.android.gangplank.easydebts.room.entities.Debtor

class AppRepository(application: Application) {

    private val db: AppDatabase = AppDatabase.getInstance(application.applicationContext)
    private val debtorDao: DebtorDao = db.debtorDao()
    private val debtDao: DebtDao = db.debtDao()
    val allDebtors: LiveData<List<Debtor>> = debtorDao.getAllDebtors()
    val allDebts: LiveData<List<Debt>> = debtDao.getAllDebts()

    // Debtors ops start

    @WorkerThread
    suspend fun insertDebtor(debtor: Debtor) {
        debtorDao.insert(debtor)
    }

    @WorkerThread
    suspend fun deleteDebtor(debtor: Debtor) {
        debtorDao.delete(debtor)
    }

    @WorkerThread
    suspend fun updateDebtor(debtor: Debtor) {
        debtorDao.update(debtor)
    }

    @WorkerThread
    suspend fun deleteAllDebtors() {
        debtorDao.deleteAllDebtors()
    }

    // Debts ops start

    @WorkerThread
    suspend fun insertDebt(debt: Debt) {
        debtDao.insert(debt)
    }

    @WorkerThread
    suspend fun deleteDebt(debt: Debt) {
        debtDao.delete(debt)
    }

    @WorkerThread
    suspend fun updateDebt(debt: Debt) {
        debtDao.update(debt)
    }

    @WorkerThread
    suspend fun deleteAllDebts() {
        debtDao.deleteAllDebts()
    }
}