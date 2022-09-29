package com.example.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import java.util.concurrent.TimeUnit


class PomodoroActivity : AppCompatActivity() {

    private lateinit var stringCurrentPeriod : TextView
    private lateinit var stringTimeRemaining : TextView
    private lateinit var buttonRestartTimer : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        stringCurrentPeriod = findViewById(R.id.current_period)
        stringTimeRemaining = findViewById(R.id.time)
        buttonRestartTimer = findViewById(R.id.restart_timer)

        buttonRestartTimer.setOnClickListener {
            startStudying()
        }
    }

    fun startStudying() {
        stringCurrentPeriod.setText("Keep on studying!")

        val timer = object : CountDownTimer(25 * 60000, 1000) {
            override fun onTick(millisUntilFinish: Long) {
                stringTimeRemaining.setText(millisToTime(millisUntilFinish))
            }

            override fun onFinish() {
                // send notification
                startBreak()
            }
        }.start()
    }

    fun startBreak() {
        stringCurrentPeriod.setText("Enjoy your break!")

        val timer = object : CountDownTimer(5 * 60000, 1000) {
            override fun onTick(millisUntilFinish: Long) {
                stringTimeRemaining.setText(millisToTime(millisUntilFinish))
            }

            override fun onFinish() {
                // send notification
                startStudying()
            }
        }.start()
    }

    fun millisToTime(millis: Long):String {
        return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
    }
}