package com.oem.drivemode.sample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oem.drivemode.sample.data.TransactionDao
import com.oem.drivemode.sample.model.Transaction
import com.oem.drivemode.sample.ui.TransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val dao: TransactionDao
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<TransactionUiState>(TransactionUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        observeTransactions()
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            dao.getAllTransactions().collect { list ->
                _uiState.value =
                    TransactionUiState.Success(
                        transactions = list,
                        showDialog = false
                    )
            }
        }
    }

    fun addTransaction(amount: Double, description: String, date: Long) {
        viewModelScope.launch {
            dao.insertTransaction(
                Transaction(
                    amount = amount,
                    description = description,
                    date = date
                )
            )
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            dao.deleteTransaction(transaction)
        }
    }

    fun showAddDialog(show: Boolean) {
        val currentState = _uiState.value
        if (currentState is TransactionUiState.Success) {
            _uiState.value = currentState.copy(
                showDialog = show
            )
        }
    }
}