package com.example.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class PomodoroActivity : AppCompatActivity() {

    private lateinit var stringCurrentPeriod : TextView
    private lateinit var stringTimeRemaining : TextView
    private lateinit var buttonStartTimer : Button
    private lateinit var buttonCancelTimer : Button

    private var timerStudy = object : CountDownTimer(25 * 60000, 1000) {
        override fun onTick(millisUntilFinish: Long) {
            stringTimeRemaining.setText(millisToTime(millisUntilFinish))
        }

        override fun onFinish() {
            val toast = Toast.makeText(applicationContext, "STUDY PERIOD COMPLETE", Toast.LENGTH_LONG)
            toast.show()
            startBreak()
        }
    }

    private var timerBreak = object : CountDownTimer(5 * 60000, 1000) {
        override fun onTick(millisUntilFinish: Long) {
            stringTimeRemaining.setText(millisToTime(millisUntilFinish))
        }

        override fun onFinish() {
            val toast = Toast.makeText(applicationContext, "BREAK PERIOD COMPLETE", Toast.LENGTH_LONG)
            toast.show()
            startStudying()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        stringCurrentPeriod = findViewById(R.id.current_period)
        stringTimeRemaining = findViewById(R.id.time)
        buttonStartTimer = findViewById(R.id.start_timer)
        buttonCancelTimer = findViewById(R.id.cancel_timer)

        buttonStartTimer.setOnClickListener {
            startStudying()
        }

        buttonCancelTimer.setOnClickListener {
            timerStudy.cancel()
            timerBreak.cancel()
            stringCurrentPeriod.setText("Keep on studying!")
            stringTimeRemaining.setText(millisToTime(25 * 60000))
        }
    }

    fun startStudying() {
        stringCurrentPeriod.setText("Keep on studying!")
        timerBreak.cancel()
        timerStudy.start()
    }

    fun startBreak() {
        stringCurrentPeriod.setText("Enjoy your break!")
        timerStudy.cancel()
        timerBreak.start()
    }

    fun millisToTime(millis: Long):String {
        return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
    }
}