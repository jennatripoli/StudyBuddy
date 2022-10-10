package com.example.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.studybuddy.viewmodels.PomodoroViewModel
import java.util.concurrent.TimeUnit

private const val totalStudy : Long = 25 * 60000
private const val totalBreak : Long = 5 * 60000

class PomodoroActivity : AppCompatActivity() {

    private lateinit var stringCurrentPeriod : TextView
    private lateinit var stringTimeRemaining : TextView
    private lateinit var buttonStartTimer : Button
    private lateinit var buttonCancelTimer : Button

    private lateinit var timerStudy : CountDownTimer
    private lateinit var timerBreak : CountDownTimer

    private val pomodoroViewModel : PomodoroViewModel by viewModels()

    private var studyOn : Boolean = false
    private var breakOn : Boolean = false

    private var currentStudyTime : Long = 0
    private var currentBreakTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        checkViewModel()

        initTimers()

        stringCurrentPeriod = findViewById(R.id.current_period)
        stringTimeRemaining = findViewById(R.id.time)
        buttonStartTimer = findViewById(R.id.start_timer)
        buttonCancelTimer = findViewById(R.id.cancel_timer)

        if(studyOn) startStudying()
        else if(breakOn) startBreak()

        buttonStartTimer.setOnClickListener {
            startStudying()
        }

        buttonCancelTimer.setOnClickListener {
            timerStudy.cancel()
            studyOn = false
            pomodoroViewModel.studyOn = studyOn
            currentStudyTime = totalStudy
            pomodoroViewModel.currentStudyTime = currentStudyTime

            timerBreak.cancel()
            breakOn = false
            pomodoroViewModel.breakOn = breakOn
            currentBreakTime = totalBreak
            pomodoroViewModel.currentBreakTime = currentBreakTime

            stringCurrentPeriod.setText("Keep on studying!")
            stringTimeRemaining.setText(millisToTime(totalStudy))

            initTimers() // timers must re-init
        }
    }

    private fun initTimers() {

        timerStudy = object : CountDownTimer(currentStudyTime, 1000) {
            override fun onTick(millisUntilFinish: Long) {
                currentStudyTime = millisUntilFinish
                pomodoroViewModel.currentStudyTime = currentStudyTime
                stringTimeRemaining.setText(millisToTime(millisUntilFinish))
            }

            override fun onFinish() {
                val toast = Toast.makeText(applicationContext, "STUDY PERIOD COMPLETE", Toast.LENGTH_LONG)
                currentStudyTime = totalStudy
                pomodoroViewModel.currentStudyTime = currentStudyTime
                toast.show()
                startBreak()
            }
        }

        timerBreak = object : CountDownTimer(currentBreakTime, 1000) {
            override fun onTick(millisUntilFinish: Long) {
                currentBreakTime = millisUntilFinish
                pomodoroViewModel.currentBreakTime = currentBreakTime
                stringTimeRemaining.setText(millisToTime(millisUntilFinish))
            }

            override fun onFinish() {
                val toast = Toast.makeText(applicationContext, "BREAK PERIOD COMPLETE", Toast.LENGTH_LONG)
                currentBreakTime = totalBreak
                pomodoroViewModel.currentBreakTime = currentBreakTime
                toast.show()
                startStudying()
            }
        }
    }

    private fun checkViewModel() {
        // get values stored in view model, default if nothing is there
        currentStudyTime = if(pomodoroViewModel.currentStudyTime != null) pomodoroViewModel.currentStudyTime!!
        else totalStudy
        currentBreakTime = if(pomodoroViewModel.currentBreakTime != null) pomodoroViewModel.currentBreakTime!!
        else totalBreak

        // get booleans from view model
        studyOn = if(pomodoroViewModel.studyOn != null) pomodoroViewModel.studyOn!!
        else false
        breakOn = if(pomodoroViewModel.breakOn != null) pomodoroViewModel.breakOn!!
        else false

    }

    fun startStudying() {
        stringCurrentPeriod.setText("Keep on studying!")

        timerBreak.cancel()
        breakOn = false
        pomodoroViewModel.breakOn = breakOn

        timerStudy.start()
        studyOn = true
        pomodoroViewModel.studyOn = studyOn
    }

    fun startBreak() {
        stringCurrentPeriod.setText("Enjoy your break!")

        timerStudy.cancel()
        studyOn = false
        pomodoroViewModel.studyOn = studyOn

        timerBreak.start()
        breakOn = true
        pomodoroViewModel.breakOn = breakOn
    }

    fun millisToTime(millis: Long):String {
        return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
    }
}