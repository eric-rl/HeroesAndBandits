package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.Items.CharacterItem
import com.example.heroesandbandits.Items.FavoriteCharacterItem
import com.example.heroesandbandits.Items.FavoriteSeriesItem
import com.example.heroesandbandits.Items.SeriesItem
import com.example.heroesandbandits.Models.FavoriteCharacter
import com.example.heroesandbandits.Models.FavoriteSeries
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.MarvelRetrofit
import com.example.heroesandbandits.Utils.StitchCon
import com.example.heroesandbandits.ViewModel.SharedViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favourites_result.view.*
import org.bson.Document

class FavoritesSeriesResultFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = activity?.let { ViewModelProviders.of(it).get(SharedViewModel::class.java) }!!
        val view = inflater.inflate(R.layout.fragment_favourites_result, container, false)
        val adapter = createRecyclerView()
        view.recyclerViewFavouritesResult.adapter = adapter
        return view
    }

    companion object {
        fun newInstance(): FavoritesSeriesResultFragment =
            FavoritesSeriesResultFragment()
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.favourite_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun createRecyclerView(): GroupAdapter<GroupieViewHolder> {
        val adapter = GroupAdapter<GroupieViewHolder>()

        for (series in StitchCon.userData?.series!!) {
            val item = series as Document
            d("___", "ett item: ${item["title"]}")
            adapter.add(
                FavoriteSeriesItem(
                    FavoriteSeries(
                        item["title"] as String,
                        item["thumbnail"] as String,
                        item["id"] as Int
                    )
                )
            )
        }

        adapter.setOnItemClickListener { item, _ ->
            val seriesItem = item as FavoriteSeriesItem
            MarvelRetrofit.marvelService.getOneSeries(seriesItem.series.id)
                .subscribeOn(Schedulers.newThread())
                .subscribe { result, err ->
                    if (err?.message != null) {
                        d("___", "Something went wrong: ${err.message}")
                    } else {
                        d("___", "$result")
                        sharedViewModel.clickedSeries = result.data.results[0]
                        replaceFragment(DetailFragment.newInstance())
                    }
                }
        }

        return adapter

    }
}