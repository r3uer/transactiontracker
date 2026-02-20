package com.oem.drivemode.sample.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oem.drivemode.sample.model.Transaction
import com.oem.drivemode.sample.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTrackerScreen(viewModel: TransactionViewModel) {

    val state by viewModel.uiState.collectAsState()

    when (state) {

        is TransactionUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading...")
            }
        }

        is TransactionUiState.Success -> {

            val data = state as TransactionUiState.Success

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Transaction Tracker") }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { viewModel.showAddDialog(true) }
                    ) {
                        Text("+")
                    }
                }
            ) { padding ->

                if (data.transactions.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No transactions yet")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(padding)
                    ) {
                        items(data.transactions) { transaction ->
                            TransactionItem(
                                transaction = transaction,
                                onDelete = {
                                    viewModel.deleteTransaction(it)
                                }
                            )
                        }
                    }
                }
            }

            if (data.showDialog) {
                AddTransactionDialog(
                    onAdd = { amount, desc, date ->
                        viewModel.addTransaction(amount, desc, date)
                        viewModel.showAddDialog(false)
                    },
                    onDismiss = {
                        viewModel.showAddDialog(false)
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onDelete: (Transaction) -> Unit
) {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("₹${transaction.amount}")
                Text(transaction.description)
                Text(formatter.format(Date(transaction.date)))
            }

            IconButton(onClick = { onDelete(transaction) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun AddTransactionDialog(
    onAdd: (Double, String, Long) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Transaction") },
        text = {
            Column {

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                    onAdd(parsedAmount, description, System.currentTimeMillis())
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}