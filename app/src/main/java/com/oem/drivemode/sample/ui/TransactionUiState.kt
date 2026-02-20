package com.oem.drivemode.sample.ui

import com.oem.drivemode.sample.model.Transaction

sealed class TransactionUiState {
    object Loading : TransactionUiState()
    data class Success(
        val transactions: List<Transaction>,
        val showDialog: Boolean = false
    ) : TransactionUiState()
}