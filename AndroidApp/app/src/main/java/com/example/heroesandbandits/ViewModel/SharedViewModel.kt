package com.example.heroesandbandits.ViewModel

import androidx.lifecycle.ViewModel
import com.example.heroesandbandits.Utils.Character

class SharedViewModel: ViewModel() {
    var searchResults = ArrayList<Character>()
}