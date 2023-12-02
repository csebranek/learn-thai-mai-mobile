package com.example.learn_thai_mai_mobile

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.learn_thai_mai_mobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var inputTranslationBox: EditText

    private lateinit var binding: ActivityMainBinding

    private var wordDictionary: HashMap<String,String> = HashMap();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize word dict
        wordDictionary["no"] = "ไม่"

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

        inputTranslationBox = findViewById(R.id.inputTranslationBox)
    }

    fun buttonClick(view: View?){
        val solution: Boolean = getTranslatedWord(inputTranslationBox.text.toString())
        val displayText: TextView = findViewById(R.id.SubmissionFeedback)
        if (solution){
            displayText.text = "Correct Answer!"
        }
        else {
            displayText.text = "Wrong Answer!"
        }
        //println("test")
    }

    private fun getTranslatedWord(word: String?): Boolean {
        return word.equals(wordDictionary["no"])
    }


}