package com.example.heroesandbandits.ViewModel

import androidx.lifecycle.ViewModel
import com.example.heroesandbandits.Models.Character


class SharedViewModel: ViewModel() {
    var searchResults = ArrayList<Character>()
}