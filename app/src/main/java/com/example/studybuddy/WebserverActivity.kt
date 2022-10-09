package com.example.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.*
import com.example.studybuddy.networkapi.JSON
import com.example.studybuddy.networkapi.NetworkAPI
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

private const val TAG = "WebserverActivity"
private const val baseURL = "https://api.dictionaryapi.dev/"

const val REQUEST_CODE_MICROPHONE = 30

class WebserverActivity : AppCompatActivity() {

    private lateinit var editTextSearch : EditText
    private lateinit var buttonSearch : Button
    private lateinit var buttonMicrophone : Button
    private lateinit var textDefinition : TextView
    private lateinit var progressBar : ProgressBar

    private lateinit var networkAPI : NetworkAPI

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
        buttonSearch = findViewById(R.id.search_btn)
        buttonMicrophone = findViewById(R.id.microphone_btn)
        textDefinition = findViewById(R.id.definition_text)
        progressBar = findViewById(R.id.progress_bar)

        progressBar.visibility = View.INVISIBLE // init as invisible

        try { // init the network api
            networkAPI = initNetworkAPI()
        } catch (e : Exception) { // exit intent if there was an issue
            Log.d(TAG, "There was an issue connection the dictionary API")
            finish()
        }

        buttonMicrophone.setOnClickListener() { // microphone button press

            // create intent
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            // put intent extras for speech recognition
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Term")

            // start intent activity
            startActivityForResult(intent, REQUEST_CODE_MICROPHONE)
        }

        buttonSearch.setOnClickListener() { // on search
            searchApiForDefinition(networkAPI)
        }
    }

    /**
     * Initializes the retrofit API and Moshi JSON parsing
     */
    private fun initNetworkAPI(): NetworkAPI {
        // declare moshi for json parsing
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

    /**
     * Searches the dictionary API at the baseurl for the inputted word
     */
    private fun searchApiForDefinition(networkAPI : NetworkAPI) {
        // start progress bar
        progressBar.visibility = View.VISIBLE

        // definition init
        var def : String

        // get term to search
        val searchTerm = editTextSearch.text.toString()

        if(searchTerm != "") { // user attempts to enter a term
            GlobalScope.launch(Dispatchers.Main){
                try {
                    val list: List<JSON> = networkAPI.fetchDefinition(searchTerm)
                    def = list[0].meanings[0].defs[0].def
                    textDefinition.text = def
                    progressBar.visibility = View.INVISIBLE
                } catch (e : Exception) {
                    progressBar.visibility = View.INVISIBLE
                    textDefinition.text = getString(R.string.unable_to_find)
                }
            }
        } else { // user did not enter a term
            progressBar.visibility = View.INVISIBLE
            val toast = Toast.makeText(applicationContext, "Please Enter a Term!", Toast.LENGTH_LONG)
            toast.show()
            return
        }
    }
}