package com.example.studybuddy

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class FlashcardActivity : AppCompatActivity() {

    private lateinit var buttonFlip : Button
    private lateinit var cardFront : TextView
    private lateinit var cardBack : TextView

    private lateinit var animationFront : AnimatorSet
    private lateinit var animationBack : AnimatorSet
    private var showingFront = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        buttonFlip = findViewById(R.id.card_flip)
        cardFront = findViewById(R.id.card_front)
        cardBack = findViewById(R.id.card_back)

        val scale = applicationContext.resources.displayMetrics.density
        cardFront.cameraDistance = 8000 * scale
        cardBack.cameraDistance = 8000 * scale

        animationFront = AnimatorInflater.loadAnimator(applicationContext, R.animator.animator_front) as AnimatorSet
        animationBack = AnimatorInflater.loadAnimator(applicationContext, R.animator.animator_back) as AnimatorSet

        buttonFlip.setOnClickListener {
            if (showingFront) {
                animationFront.setTarget(cardFront)
                animationBack.setTarget(cardBack)
                animationFront.start()
                animationBack.start()
                showingFront = false
            } else {
                animationFront.setTarget(cardBack)
                animationBack.setTarget(cardFront)
                animationBack.start()
                animationFront.start()
                showingFront = true
            }
        }
    }
}