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
    private lateinit var buttonNext : Button
    private lateinit var buttonPrevious : Button
    private lateinit var cardFront : TextView
    private lateinit var cardBack : TextView
    private lateinit var stringStudySetName : String
    private lateinit var flashcardList: List<FlashcardEntity>
    private var currentIndex = 0

    private lateinit var animationFront : AnimatorSet
    private lateinit var animationBack : AnimatorSet
    private var showingFront = true

    private val db = AppDatabase.getInstance(BaseApplication.getContext())?.dao

    private val flashcardActivityViewModel : FlashcardActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        stringStudySetName = intent.getStringExtra(EXTRA_FLASHCARD_SET).toString() // gets the name of the study set to pull from

        currentIndex = if(flashcardActivityViewModel.currentIndex == null) 0 // pull from view model to save state on screen rotation
        else flashcardActivityViewModel.currentIndex!!

        buttonFlip = findViewById(R.id.card_flip)
        buttonNext = findViewById(R.id.next_term)
        buttonPrevious = findViewById(R.id.previous_term)
        cardFront = findViewById(R.id.card_front)
        cardBack = findViewById(R.id.card_back)

        val scale = applicationContext.resources.displayMetrics.density // init all animation properties
        cardFront.cameraDistance = 8000 * scale
        cardBack.cameraDistance = 8000 * scale

        animationFront = AnimatorInflater.loadAnimator(applicationContext, R.animator.animator_front) as AnimatorSet
        animationBack = AnimatorInflater.loadAnimator(applicationContext, R.animator.animator_back) as AnimatorSet

        GlobalScope.launch { // init the flashcard list and initial flashcard
            flashcardList = db?.getFlashcardsForStudySet(stringStudySetName)!!
            displayTerm()
            displayDef()
        }

        buttonFlip.setOnClickListener { // flips the flashcard
            if (showingFront) {
                flipToBack()
            } else {
                flipToFront()
            }
        }

        buttonNext.setOnClickListener() { // go next
            currentIndex = (currentIndex + 1) % flashcardList.size
            flashcardActivityViewModel.currentIndex = currentIndex
            handleCardChange()
        }

        buttonPrevious.setOnClickListener() { // go previous
            currentIndex = (currentIndex - 1) % flashcardList.size
            if(currentIndex < 0) currentIndex = flashcardList.size - 1
            flashcardActivityViewModel.currentIndex = currentIndex
            handleCardChange()
        }
    }

    private fun handleCardChange() {
        if(showingFront) {
            displayTerm()
        } else {
            displayDef()
        }
    }

    private fun flipToBack() {
        animationFront.setTarget(cardFront)
        animationBack.setTarget(cardBack)
        animationFront.start()
        animationBack.start()
        showingFront = false
        displayDef()
    }

    private fun flipToFront() {
        animationFront.setTarget(cardBack)
        animationBack.setTarget(cardFront)
        animationBack.start()
        animationFront.start()
        showingFront = true
        displayTerm()
    }

    private fun displayTerm() {
        cardFront.text = flashcardList[currentIndex].term
    }

    private fun displayDef() {
        cardBack.text = flashcardList[currentIndex].definition
    }

}