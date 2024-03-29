package com.csebranek.learn_thai_mai_mobile

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.csebranek.learn_thai_mai_mobile.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var inputTranslationBox: EditText
    private lateinit var currentWordToTranslate: TextView
    private lateinit var translateBoxLabel: String
    private lateinit var appBarConfiguration: AppBarConfiguration
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


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_settings, R.id.navigation_learn, R.id.navigation_statistics
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        currentWordToTranslate = findViewById(R.id.wordToTranslate)
        setWord(currentWord.english)
        translateBoxLabel = currentWordToTranslate.text.toString()
        currentWordToTranslate.text = translateBoxLabel.plus(currentWord.english)
        inputTranslationBox = findViewById(R.id.inputTranslationBox)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }




    fun buttonClick(view: View?) {
        val displayText: TextView = findViewById(R.id.SubmissionFeedback)
        var currentDisplay: TextView = findViewById(R.id.wordToTranslate)
        displayText.text =
            if (currentWord.thai == inputTranslationBox.text.toString()) getString(R.string.correct_answer) else getString(R.string.wrong_answer).plus(getString(R.string.wrong_answer_fdbk)).plus(currentWord.thai)
        nextWord()
        currentDisplay.text = currentWord.english
    }


    fun playThaiSound(view: View?) {
        //strip question marks
        var audioPath: String = "sounds/".plus(currentWord.english.plus(".m4a").replace("?", ""));
        audioPlayer(audioPath);
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

    companion object {
        private lateinit var savedWord: String

        fun restoreWord(): String {
            return savedWord;
        }

        fun setWord(_theWord: String) {
            this.savedWord = _theWord
        }
    }


}