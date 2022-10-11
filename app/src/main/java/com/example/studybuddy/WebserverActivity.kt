package com.example.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import com.example.studybuddy.networkapi.JSON
import com.example.studybuddy.networkapi.NetworkAPI
import com.example.studybuddy.viewmodels.WebserverDefViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.util.*
import kotlin.collections.ArrayList

const val REQUEST_CODE_MICROPHONE = 5
private const val baseURL = "https://api.dictionaryapi.dev/"

class WebserverActivity : AppCompatActivity() {

    private lateinit var buttonSearch : Button
    private lateinit var buttonMicrophone : Button
    private lateinit var editTextSearch : EditText
    private lateinit var textDefinition : TextView
    private lateinit var progressBar : ProgressBar

    private lateinit var networkAPI : NetworkAPI

    private val webserverDefViewModel : WebserverDefViewModel by viewModels()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_MICROPHONE && resultCode == RESULT_OK && data != null) {
            val spokenArray : ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
            editTextSearch.setText(spokenArray.joinToString(separator = " ").trim())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webserver)

        editTextSearch = findViewById(R.id.search_text)
        buttonSearch = findViewById(R.id.search_vocab)
        buttonMicrophone = findViewById(R.id.microphone)
        textDefinition = findViewById(R.id.definition_text)
        progressBar = findViewById(R.id.progress_bar)

        // init as invisible
        progressBar.visibility = View.INVISIBLE

        // init the network api, exit intent if there was an issue
        try {
            networkAPI = initNetworkAPI()
        } catch (e : Exception) {
            finish()
        }

        // check view model
        if(webserverDefViewModel.currentDef != null) textDefinition.text = webserverDefViewModel.currentDef

        buttonMicrophone.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            // put intent extras for speech recognition
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Term")

            startActivityForResult(intent, REQUEST_CODE_MICROPHONE)
        }

        buttonSearch.setOnClickListener {
            searchApiForDefinition(networkAPI)
        }
    }

    // init the retrofit API and Moshi JSON parsing
    private fun initNetworkAPI(): NetworkAPI {
        // declare Moshi for JSON parsing
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        // create retrofit object at the base url
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        // create a network api interface and return
        return retrofit.create()
    }

    // search the dictionary API at the base url for the inputted word
    private fun searchApiForDefinition(networkAPI : NetworkAPI) {
        // start progress bar
        progressBar.visibility = View.VISIBLE

        // definition init
        var def : String

        // get term to search
        val searchTerm = editTextSearch.text.toString()

        // user attempts to enter a term
        if(searchTerm != "") {
            GlobalScope.launch(Dispatchers.Main){
                try {
                    val list: List<JSON> = networkAPI.fetchDefinition(searchTerm)
                    def = list[0].meanings[0].defs[0].def
                    webserverDefViewModel.currentDef = def
                    textDefinition.text = def
                    progressBar.visibility = View.INVISIBLE
                } catch (e : Exception) {
                    progressBar.visibility = View.INVISIBLE
                    textDefinition.text = getString(R.string.unable_to_find)
                }
            }

        // user did not enter a term
        } else {
            progressBar.visibility = View.INVISIBLE
            val toast = Toast.makeText(applicationContext, "Please enter a term.", Toast.LENGTH_LONG)
            toast.show()
            return
        }
    }
}