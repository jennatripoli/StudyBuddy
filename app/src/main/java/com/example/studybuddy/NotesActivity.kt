package com.example.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.databinding.ActivityNotesBinding
import com.squareup.picasso.Picasso

private const val TAG = "NotesActivity"

class NotesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNotesBinding // use bindings here to get widget references
    private lateinit var noteRecyclerView : RecyclerView
    private lateinit var takePhotoButton : Button

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { photoTaken : Boolean ->
        if(photoTaken) {
         Log.d(TAG, "Photo was taken")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_notes)

        // init picasso
        Picasso.get().setIndicatorsEnabled(true)

        binding.floatBtn.setOnClickListener() { // on take photo

        }
    }
}