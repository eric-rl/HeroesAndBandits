package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.Items.SeriesItem
import com.example.heroesandbandits.Models.Series
import com.example.heroesandbandits.R
import com.example.heroesandbandits.ViewModel.SharedViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_search_result.view.*

class SearchSeriesResultFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = activity?.let { ViewModelProviders.of(it).get(SharedViewModel::class.java) }!!
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)
        val adapter = createRecyclerView()
        view.recyclerViewSearchResult.adapter = adapter
        return view
    }

    companion object {
        fun newInstance(): SearchSeriesResultFragment =
            SearchSeriesResultFragment()
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_view, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun createRecyclerView(): GroupAdapter<GroupieViewHolder> {
        val adapter = GroupAdapter<GroupieViewHolder>()

        for(series in sharedViewModel.searchResultsSeries){
            adapter.add(SeriesItem(Series(series.id, series.title, series.description, series.thumbnail, series.urls)))
        }

        adapter.setOnItemClickListener { item, _ ->
            val series = item as SeriesItem
            sharedViewModel.clickedSeries = series.series
            replaceFragment(DetailFragment.newInstance())
        }
        return adapter

    }
}