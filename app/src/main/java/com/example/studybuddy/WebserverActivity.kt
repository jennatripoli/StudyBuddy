package com.example.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.studybuddy.networkapi.NetworkAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

private const val TAG = "WebserverActivity"
private const val baseURL = "https://api.dictionaryapi.dev/"

class WebserverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webserver)


        // declare moshi for json parsing
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        // create retrofit object at the base url
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        // create a network api interface
        val networkAPI: NetworkAPI = retrofit.create()

        GlobalScope.launch {
            val JSON = networkAPI.fetchDefinition()
            Log.d(TAG, "$JSON")
        }




    }
}