package com.example.learn_thai_mai_mobile.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.learn_thai_mai_mobile.MainActivity
import com.example.learn_thai_mai_mobile.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    val vm: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {




        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if (savedInstanceState != null) {
            var someStateValue = savedInstanceState.getInt("theBoolean");
            println("The saved val: ")
            print(someStateValue)
            // Do something with value if needed
        }

        val textView: TextView = binding.textHome
        val translatedWord: TextView = binding.wordToTranslate
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
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