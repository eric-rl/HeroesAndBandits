package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.R
import com.example.heroesandbandits.ViewModel.SharedViewModel

class MessageFragment : Fragment() {

//    private val sharedViewModel by lazy {
//        activity?.let { ViewModelProviders.of(it).get(SharedViewModel::class.java) }
//    }

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = activity?.let { ViewModelProviders.of(it).get(SharedViewModel::class.java) }!!
        val v = inflater.inflate(R.layout.fragment_message, container, false)
        d("blablabla", "${sharedViewModel.searchResults}")
        return v
    }

    companion object {
        fun newInstance(): MessageFragment =
            MessageFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("__", "hej från message")

    }
}