package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.heroesandbandits.Items.HeroItem
import com.example.heroesandbandits.Models.Hero
import com.example.heroesandbandits.R
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)

        replaceFragment(SearchDefaultFragment.newInstance())

        v.search_button.setOnClickListener {
            replaceFragment(SearchResultFragment.newInstance())
            //replaceFragment(SearchNoResultFragment.newInstance())
        }

        return v
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(): SearchFragment =
            SearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("__", "hej från search")
    }
}

