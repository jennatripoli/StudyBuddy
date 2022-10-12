package com.example.studybuddy

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.example.studybuddy.database.AppDatabase
import com.example.studybuddy.database.FlashcardEntity
import com.example.studybuddy.viewmodels.FlashcardActivityViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FlashcardActivity : AppCompatActivity() {

    private lateinit var buttonFlip : Button
    private lateinit var buttonNext : Button
    private lateinit var buttonPrevious : Button
    private lateinit var cardFront : TextView
    private lateinit var cardBack : TextView
    private lateinit var stringStudySetName : String
    private lateinit var textStudySetName : TextView

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

        // get the name of the study set to pull from
        stringStudySetName = intent.getStringExtra(EXTRA_FLASHCARD_SET).toString()

        // pull from view model to save state on screen rotation
        currentIndex = if(flashcardActivityViewModel.currentIndex == null) 0
        else flashcardActivityViewModel.currentIndex!!

        buttonFlip = findViewById(R.id.card_flip)
        buttonNext = findViewById(R.id.next_term)
        buttonPrevious = findViewById(R.id.previous_term)
        cardFront = findViewById(R.id.card_term)
        cardBack = findViewById(R.id.card_definition)
        textStudySetName = findViewById(R.id.flashcard_study_set_name)
        textStudySetName.text = stringStudySetName + " FLASHCARDS"

        // init all animation properties
        val scale = applicationContext.resources.displayMetrics.density
        cardFront.cameraDistance = 8000 * scale
        cardBack.cameraDistance = 8000 * scale

        animationFront = AnimatorInflater.loadAnimator(applicationContext, R.animator.animator_front) as AnimatorSet
        animationBack = AnimatorInflater.loadAnimator(applicationContext, R.animator.animator_back) as AnimatorSet

        // init the flashcard list and initial flashcard
        GlobalScope.launch {
            flashcardList = db?.getFlashcardsForStudySet(stringStudySetName)!!
            displayTerm()
            displayDef()
        }

        // flip the flashcard to show the other side
        buttonFlip.setOnClickListener {
            if (showingFront) {
                flipToBack()
            } else {
                flipToFront()
            }
        }

        // move to next term
        buttonNext.setOnClickListener() {
            currentIndex = (currentIndex + 1) % flashcardList.size
            flashcardActivityViewModel.currentIndex = currentIndex
            handleCardChange()
        }

        // move to previous term
        buttonPrevious.setOnClickListener() {
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