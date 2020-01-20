package com.example.heroesandbandits.ViewModel

import androidx.lifecycle.ViewModel
import com.example.heroesandbandits.Models.Character
import com.xwray.groupie.Item


class SharedViewModel: ViewModel() {
    var searchResults = ArrayList<Character>()
    var clickedItem: Character? = null
}