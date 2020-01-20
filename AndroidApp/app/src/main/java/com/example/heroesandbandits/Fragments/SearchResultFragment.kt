package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.Items.HeroItem
import com.example.heroesandbandits.Models.Hero
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.Character
import com.example.heroesandbandits.ViewModel.SharedViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_search_result.view.*
import java.util.*

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
            d("__", "Hej från metoden create recyclerView")
            val adapter = GroupAdapter<GroupieViewHolder>()

            for (character in sharedViewModel.searchResults) {
                d("hejhej", character.name)

                adapter.add(HeroItem(Hero(character.name, 20)))
            }
        return adapter

    }
}