package com.example.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
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

private const val TAG = "WebserverActivity"
private const val baseURL = "https://api.dictionaryapi.dev/"

class WebserverActivity : AppCompatActivity() {

    private lateinit var editTextSearch : EditText
    private lateinit var buttonSearch : Button
    private lateinit var textDefinition : TextView
    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webserver)

        editTextSearch = findViewById(R.id.search_text)
        buttonSearch = findViewById(R.id.search_btn)
        textDefinition = findViewById(R.id.definition_text)
        progressBar = findViewById(R.id.progress_bar)

        // declare moshi for json parsing
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        // create retrofit object at the base url
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        // create a network api interface
        val networkAPI: NetworkAPI = retrofit.create()

        // progress bar is invisible
        progressBar.visibility = View.INVISIBLE

        buttonSearch.setOnClickListener() {

            // start progress bar
            progressBar.visibility = View.VISIBLE

            // definition init
            var def = ""

            // get term to search
            val searchTerm = editTextSearch.text.toString()

            if(searchTerm != "") {
                GlobalScope.launch(Dispatchers.Main){
                    val list : List<JSON> = networkAPI.fetchDefinition(searchTerm)
                    def = list[0].meanings[0].defs[0].def
                    textDefinition.text = def
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }
}