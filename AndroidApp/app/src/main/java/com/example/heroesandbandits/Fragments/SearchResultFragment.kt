package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.heroesandbandits.Items.HeroItem
import com.example.heroesandbandits.Models.Hero
import com.example.heroesandbandits.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_search_result.view.*

class SearchResultFragment : Fragment() {

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("__", "Hej från onCreateView")

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
        adapter.add(HeroItem(Hero("kdwadwadawsjdghfksd", 23)))
        adapter.add(HeroItem(Hero("ksjdghfksd", 23)))
        adapter.add(HeroItem(Hero("ksjdghfksd", 23)))
        adapter.add(HeroItem(Hero("ksjdghfksd", 23)))
        adapter.add(HeroItem(Hero("ksjdghfksd", 23)))
        adapter.add(HeroItem(Hero("ksjdghfksd", 23)))
        adapter.add(HeroItem(Hero("ksjdghfksd", 23)))
        adapter.add(HeroItem(Hero("ksjdghfksd", 23)))
        adapter.add(HeroItem(Hero("ksjdghfksd", 23)))
        adapter.add(HeroItem(Hero("ksjdghfksd", 23)))
        return adapter

    }
}