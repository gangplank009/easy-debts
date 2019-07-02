package com.android.gangplank.easydebts.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "debt_table",
        foreignKeys = arrayOf(ForeignKey(
            entity = Debtor::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("debtor_id"),
            onDelete = ForeignKey.CASCADE)))
data class Debt(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "debtor_id", index = true)
    var debtorId: Long,

    @ColumnInfo(name = "debt_value")
    var value: Long,

    @ColumnInfo(name = "debt_start_date")
    var startDate: Date,

    @ColumnInfo(name = "debt_checked")
    var state: Boolean = false
)