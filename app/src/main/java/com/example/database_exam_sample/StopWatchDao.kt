package com.example.database_exam_sample

import androidx.room.*

@Dao
interface StopWatchDao {
    @Insert
    fun insert(stopWatch : StopWatch)

    @Update
    fun update(stopWatch : StopWatch)

    @Delete
    fun delete(stopWatch : StopWatch)

    @Query("select * from stopwatches order by id limit 1")
    fun getFirst(): StopWatch

    @Query("select count(*) from stopwatches")
    fun getCount(): Int

    @Query("delete from stopwatches")
    fun deleteAll()

    @Query("select * from stopwatches")
    fun getAll(): List<StopWatch>
}