package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.Items.CharacterItem
import com.example.heroesandbandits.Models.Character
import com.example.heroesandbandits.R
import com.example.heroesandbandits.ViewModel.SharedViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_search_result.view.*

class FavoritesFragment: Fragment() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = activity?.let { ViewModelProviders.of(it).get(SharedViewModel::class.java) }!!
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        val adapter = createRecyclerView()
        view.recyclerViewTest.adapter = adapter

        return view
    }
//        inflater.inflate(R.layout.fragment_favorites, container, false)


    companion object {
        fun newInstance(): FavoritesFragment =
            FavoritesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun createRecyclerView(): GroupAdapter<GroupieViewHolder> {
        val adapter = GroupAdapter<GroupieViewHolder>()


        for(char in sharedViewModel.searchResultsCharacter){
            adapter.add(CharacterItem(Character(char.name, char.description, char.id, char.thumbnail, char.urls)))
        }

//        adapter.setOnItemClickListener { item, _ ->
//            val char = item as CharacterItem
//            sharedViewModel.clickedItem = char.character
//            replaceFragment(DetailFragment.newInstance())
//        }

        return adapter

    }
}