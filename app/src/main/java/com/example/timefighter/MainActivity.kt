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
    internal val countDownInterval: Long = 1000

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)

        Log.d(TAG, "onCreate called. Score is: $score")
        resetGame()
        tapMeButton.setOnClickListener { _ ->
            incrementScore()
        }
    }

    private fun resetGame() {
        score = 0
        gameScoreTextView.text = getString(R.string.yourScore, score)
        timeLeftTextView.text = getString(R.string.timeLeft, initialCountDown/1000)
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
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
}