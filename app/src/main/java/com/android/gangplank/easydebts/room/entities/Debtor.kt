package com.android.gangplank.easydebts.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debtor_table")
data class Debtor(
    @ColumnInfo(name = "debtor_name")
    var name: String,

    @ColumnInfo(name = "debtor_tel_number")
    var telNumber: String,

    @ColumnInfo(name = "debtor_avatar", typeAffinity = ColumnInfo.BLOB)
    var avatar: ByteArray? = byteArrayOf(0),

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

)
