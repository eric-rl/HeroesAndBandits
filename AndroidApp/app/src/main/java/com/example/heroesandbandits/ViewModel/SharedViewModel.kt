package com.example.heroesandbandits.ViewModel

import androidx.lifecycle.ViewModel
import com.example.heroesandbandits.Models.Character
import com.example.heroesandbandits.Models.FavoriteCharacter
import com.example.heroesandbandits.Models.Series


class SharedViewModel: ViewModel() {
    var searchResultsCharacter = ArrayList<Character>()
    var clickedItem: Character? = null
    var clickedSeries: Series? = null
    var searchResultsSeries = ArrayList<Series>()
    var searchForCharacters: Boolean = true
}