package com.example.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    internal var score = 0
    internal var isGameStarted = false

    internal lateinit var tapMeButton: Button
    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView

    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 60000
    internal var timeLeftOnTimer: Long = 60000
    internal val countDownInterval: Long = 1000

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)

        Log.d(TAG, "onCreate called. Score is: $score")
        tapMeButton.setOnClickListener { _ ->
            incrementScore()
        }
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    private fun restoreGame() {
        gameScoreTextView.text = getString(R.string.yourScore, score)
        timeLeftTextView.text = getString(R.string.timeLeft, timeLeftOnTimer/1000)
        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                timeLeftTextView.text = getString(R.string.timeLeft, millisUntilFinished/1000)
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        isGameStarted = true
    }

    private fun resetGame() {
        score = 0
        gameScoreTextView.text = getString(R.string.yourScore, score)
        timeLeftTextView.text = getString(R.string.timeLeft, initialCountDown/1000)
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                timeLeftTextView.text = getString(R.string.timeLeft, millisUntilFinished/1000)
            }

            override fun onFinish() {
                endGame()
            }
        }
        isGameStarted = false
    }

    private fun incrementScore() {
        if (!isGameStarted) {
            startGame()
        }
        score += 1
        val newScore = getString(R.string.yourScore, score)
        gameScoreTextView.text = newScore
    }

    private fun startGame() {
        countDownTimer.start()
        isGameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_SHORT).show()
        resetGame()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState, score: $score timeLeftOnTimer: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}