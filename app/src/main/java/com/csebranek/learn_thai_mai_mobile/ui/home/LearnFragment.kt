package com.csebranek.learn_thai_mai_mobile.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.csebranek.learn_thai_mai_mobile.MainActivity
import com.csebranek.learn_thai_mai_mobile.databinding.FragmentLearnBinding


class LearnFragment : Fragment() {

    val vm: LearnViewModel by viewModels()

    private var _binding: FragmentLearnBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel =
            ViewModelProvider(this).get(LearnViewModel::class.java)

        _binding = FragmentLearnBinding.inflate(inflater, container, false)
        val root: View = _binding!!.root
        if (savedInstanceState != null) {
            var someStateValue = savedInstanceState.getInt("theBoolean");
            println("The saved val: ")
            print(someStateValue)
            // Do something with value if needed
        }

        return root
    }
    override fun onPause (){
        super.onPause();
        if (_binding?.wordToTranslate!!.text.toString() != ""){
            MainActivity.setWord(_binding?.wordToTranslate!!.text.toString())
        }
    }

    override fun onResume() {
        super.onResume();
        val translatedWord: TextView = _binding!!.wordToTranslate
        vm.text.observe(viewLifecycleOwner) {
           translatedWord.text = MainActivity.restoreWord()
       }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("theBoolean", true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}