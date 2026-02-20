package com.oem.drivemode.sample.di

import android.content.Context
import androidx.room.Room
import com.oem.drivemode.sample.data.TransactionDao
import com.oem.drivemode.sample.data.TransactionDatabase // Replace with your actual Database class name
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TransactionDatabase {
        return Room.databaseBuilder(
            context,
            TransactionDatabase::class.java,
            "transaction_db"
        ).build()
    }

    @Provides
    fun provideTransactionDao(database: TransactionDatabase): TransactionDao {
        return database.transactionDao()
    }
}