package com.android.gangplank.easydebts.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debtor_table")
data class Debtor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "debtor_name")
    val name: String,

    @ColumnInfo(name = "debtor_tel_number")
    val telNumber: String,

    @ColumnInfo(name = "debtor_avatar", typeAffinity = ColumnInfo.BLOB)
    val avatar: ByteArray? = null
)
