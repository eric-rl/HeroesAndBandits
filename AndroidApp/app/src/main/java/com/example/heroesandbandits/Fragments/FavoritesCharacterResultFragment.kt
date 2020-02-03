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
import com.example.heroesandbandits.Models.FavoriteCharacter
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.StitchCon
import com.example.heroesandbandits.ViewModel.SharedViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_favourites_result.view.*
import org.bson.Document

class FavoritesCharacterResultFragment : Fragment() {
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
        fun newInstance(): FavoritesCharacterResultFragment =
            FavoritesCharacterResultFragment()
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.favourite_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun createRecyclerView(): GroupAdapter<GroupieViewHolder> {
        val adapter = GroupAdapter<GroupieViewHolder>()

        for (char in StitchCon.userData?.characters!!) {
            val item = char as Document
            d("___", "ett item: ${item["name"]}")
            adapter.add(
                FavoriteCharacterItem(
                    FavoriteCharacter(
                        item["name"] as String,
                        item["thumbnail"] as String,
                        item["id"] as Int
                    )
                )
            )
        }

        adapter.setOnItemClickListener { item, _ ->
            val char = item as CharacterItem
            sharedViewModel.clickedItem = char.character
            replaceFragment(DetailFragment.newInstance())
        }

        return adapter

    }
}