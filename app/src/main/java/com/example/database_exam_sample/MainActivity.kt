package com.example.database_exam_sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler(Looper.getMainLooper())

        GlobalScope.launch { // launch a new coroutine in background and continue
            roomDemo(handler)
        }

        val startButton = findViewById<Button>(R.id.start_rap)
        startButton.setOnClickListener { rapHandler(handler) }

        val resetButton = findViewById<Button>(R.id.reset)
        resetButton.setOnClickListener { resetHandler(handler) }
    }

    private fun resetHandler(handler: Handler) {
        GlobalScope.launch { // launch a new coroutine in background and continue
            reset(handler)
        }
    }

    private fun reset(handler: Handler) {
        val stopWatchDao = AppDatabase.getDatabase(application).stopWatchDao()
        stopWatchDao.deleteAll()

        handler.post {
            val linearLayout: LinearLayout = findViewById(R.id.linear)
            linearLayout.removeAllViews()
        }
    }

    private fun rapHandler(handler: Handler) {
        GlobalScope.launch { // launch a new coroutine in background and continue
            rap(handler)
        }
    }

    private fun rap(handler: Handler) {
        val stopWatchDao = AppDatabase.getDatabase(application).stopWatchDao()

        val record = StopWatch(0, Date().time.toString())
        stopWatchDao.insert(record)

        val count = stopWatchDao.getCount()
        if (count > 1) {
            val firstStop = Date(stopWatchDao.getFirst().stopTime.toLong())
            val thisStop = Date(record.stopTime.toLong())

            addTextView(handler, firstStop, thisStop, count - 1)
        }
    }

    private fun roomDemo(handler: Handler) {
        val stopWatchDao = AppDatabase.getDatabase(application).stopWatchDao()
        var count = 1
        if (stopWatchDao.getCount() > 0) {
            var firstStop: Date? = null
            for (stopWatch in stopWatchDao.getAll()) {
                if (firstStop == null) {
                    firstStop = Date(stopWatch.stopTime.toLong())
                    continue
                }

                val thisStop = Date(stopWatch.stopTime.toLong())
                addTextView(handler, firstStop, thisStop, count)
                count++
            }
        }
    }

    private fun addTextView(handler: Handler,firstTime: Date, stopTime: Date, rapNo: Int) {
        val linearLayout: LinearLayout = findViewById(R.id.linear)

        val diffTime = stopTime.time - firstTime.time

        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffTime)
        val diffInMin: Long = TimeUnit.MILLISECONDS.toMinutes(diffTime) % 60
        val diffInSec: Long = TimeUnit.MILLISECONDS.toSeconds(diffTime) % 60

        handler.post {
            val text = TextView(this)

            var label: String = "rap #$rapNo:"
            if (diffInHours > 0) label += " $diffInHours hours"
            label += " $diffInMin minutes $diffInSec seconds"

            Log.v("TAG", label)

            text.text = label
            text.gravity = Gravity.CENTER
            text.textSize = 15F
            linearLayout.addView(text)
        }
    }
}