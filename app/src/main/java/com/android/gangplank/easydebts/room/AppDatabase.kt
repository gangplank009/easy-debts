package com.android.gangplank.easydebts.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.gangplank.easydebts.room.entities.Converters
import com.android.gangplank.easydebts.room.entities.Debt
import com.android.gangplank.easydebts.room.entities.Debtor
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Database(entities = arrayOf(Debtor::class, Debt::class), version = 1)
@TypeConverters(value = [Converters::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun debtorDao(): DebtorDao
    abstract fun debtDao(): DebtDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "app_db")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            Executors.newSingleThreadExecutor().execute {
                                getInstance(context).debtorDao().apply {
                                    this.insert(Debtor(1, "Pavel", "+77777777"))
                                    this.insert(Debtor(2, "Max", "+88888888"))
                                    this.insert(Debtor(3, "Dan", "+9999999"))
                                }
                            }
                        }
                    }).build()
                return INSTANCE!!
            }
        }
    }
}