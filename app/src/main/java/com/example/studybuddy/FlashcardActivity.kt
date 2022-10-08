package com.example.studybuddy

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.example.studybuddy.database.AppDatabase
import com.example.studybuddy.database.FlashcardEntity
import com.example.studybuddy.viewmodels.FlashcardActivityViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "FlashcardActivity"

class FlashcardActivity : AppCompatActivity() {

    private lateinit var buttonFlip : Button
    private lateinit var cardFront : TextView
    private lateinit var cardBack : TextView
    private lateinit var stringStudySetName : String

    private val db = AppDatabase.getInstance(BaseApplication.getContext())?.dao
    private lateinit var flashcardList: List<FlashcardEntity>

    private val flashcardActivityViewModel : FlashcardActivityViewModel by viewModels()

    private lateinit var animationFront : AnimatorSet
    private lateinit var animationBack : AnimatorSet
    private var showingFront = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        stringStudySetName = intent.getStringExtra(EXTRA_FLASHCARD_SET).toString() // gets the name of the study set to pull from

        GlobalScope.launch {
            flashcardList = db?.getFlashcardsForStudySet(stringStudySetName)!!
            Log.d(TAG, "$flashcardList")
        }

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