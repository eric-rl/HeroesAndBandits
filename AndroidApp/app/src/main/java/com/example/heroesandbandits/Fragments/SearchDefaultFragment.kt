package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.heroesandbandits.R

class SearchDefaultFragment: Fragment (){
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_search_default, container, false)

    companion object {
        fun newInstance(): SearchDefaultFragment =
            SearchDefaultFragment()
    }
}