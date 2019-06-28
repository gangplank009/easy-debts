package com.android.gangplank.easydebts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.gangplank.easydebts.room.entities.Debt
import com.android.gangplank.easydebts.room.entities.Debtor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DebtorsViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository: AppRepository = AppRepository(application)
    val allDebtors: LiveData<List<Debtor>>
    val allDebts: LiveData<List<Debt>>

    init {
        allDebtors = appRepository.allDebtors
        allDebts = appRepository.allDebts
    }

    fun insertDebtor(debtor: Debtor) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.insertDebtor(debtor)
        }
    }

    fun deleteDebtor(debtor: Debtor) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.deleteDebtor(debtor)
        }
    }

    fun updateDebtor(debtor: Debtor) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateDebtor(debtor)
        }
    }

    fun deleteAllDebtors() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.deleteAllDebtors()
        }
    }

    fun insertDebt(debt: Debt) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.insertDebt(debt)
        }
    }

    fun deleteDebt(debt: Debt) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.deleteDebt(debt)
        }
    }

    fun updateDebt(debt: Debt) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateDebt(debt)
        }
    }

    fun deleteAllDebts() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.deleteAllDebts()
        }
    }
}