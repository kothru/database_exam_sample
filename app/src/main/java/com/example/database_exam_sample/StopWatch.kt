package com.example.database_exam_sample

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stopwatches")
data class StopWatch(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "stop_time") val stopTime: String
)