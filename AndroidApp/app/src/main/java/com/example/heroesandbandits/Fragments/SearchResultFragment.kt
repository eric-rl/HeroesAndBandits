package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.util.Log
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

class SearchResultFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = activity?.let { ViewModelProviders.of(it).get(SharedViewModel::class.java) }!!
        Log.d("__", "Hej från onCreateView resultfragment")
        Log.d("____", "${sharedViewModel.searchResults}")
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)
        val adapter = createRecyclerView()
        view.recyclerViewTest.adapter = adapter
        return view
    }

    companion object {
        fun newInstance(): SearchResultFragment =
            SearchResultFragment()
    }

    private fun createRecyclerView(): GroupAdapter<GroupieViewHolder> {
        Log.d("__", "Hej från metoden create recyclerView")
        val adapter = GroupAdapter<GroupieViewHolder>()

        for(char in sharedViewModel.searchResults){
            adapter.add(CharacterItem(Character(char.name, char.description, char.id, char.thumbnail)))
        }

        return adapter

    }
}