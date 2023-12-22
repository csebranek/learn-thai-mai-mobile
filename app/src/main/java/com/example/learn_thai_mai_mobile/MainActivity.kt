package com.example.learn_thai_mai_mobile

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.learn_thai_mai_mobile.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var inputTranslationBox: EditText

    private lateinit var currentWordToTranslate: TextView
    private lateinit var translateBoxLabel: String

    private lateinit var binding: ActivityMainBinding

    private var wordDictionary: ArrayList<Word> = ArrayList()

    private lateinit var currentWord: Word

    private lateinit var homeFrag: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonFileString = getJsonDataFromAsset(applicationContext, "phrases.json")
        val gson = Gson()
        val listWordType = object : TypeToken<List<Word>>() {}.type

        if (jsonFileString != null) {
            val words: List<Word> = gson.fromJson(jsonFileString, listWordType)
            words.forEachIndexed { _, word ->
                //val a : Category = enumValueOrNull(word.category)!!
                //Category.values().find { it.name == word.category }
                wordDictionary.add(word)
            }
        }
        currentWord = wordDictionary[Random.nextInt(0,wordDictionary.size)]

        if (savedInstanceState != null) {
            //Restore the fragment's instance - TODO later
            homeFrag =
                supportFragmentManager.getFragment(savedInstanceState, "myFragmentName")!!
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //TODO: navigating between views destroys the currentWord?
        //figure out fragment keeping state later
        //NavController.OnDestinationChangedListener(navController, )

        currentWordToTranslate = findViewById(R.id.wordToTranslate)
        translateBoxLabel = currentWordToTranslate.text.toString()
        currentWordToTranslate.text = translateBoxLabel.plus(currentWord.english)
        inputTranslationBox = findViewById(R.id.inputTranslationBox)
    }


    fun buttonClick(view: View?) {
        val displayText: TextView = findViewById(R.id.SubmissionFeedback)
        displayText.text =
            if (currentWord.thai == inputTranslationBox.text.toString()) "Correct Answer!" else "Wrong Answer!"
        nextWord()
    }

    fun playThaiSound(view: View?) {
        audioPlayer("sounds/".plus(currentWord.english).plus(".m4a"))
    }

    private fun nextWord() {
        currentWord = wordDictionary[Random.nextInt(0,wordDictionary.size)]
        setCurrentWord(currentWord)
    }

    private fun setCurrentWord(word: Word?) {
        if (word != null) {
            currentWordToTranslate.text = translateBoxLabel.plus(word.english)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        supportFragmentManager.putFragment(outState, "myFragmentName",homeFrag)
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
    fun audioPlayer(fileName: String) {
        //set up MediaPlayer
        val mp = MediaPlayer()
        try {
            val value = applicationContext . assets . openFd(fileName)
            mp.setDataSource(value);
            mp.prepare()
            mp.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}