package com.example.heroesandbandits.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.Models.URLS
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.StitchCon
import com.example.heroesandbandits.ViewModel.SharedViewModel
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_search_item.view.*
import org.bson.Document

class DetailFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel =
            activity?.let { ViewModelProviders.of(it).get(SharedViewModel::class.java) }!!
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        return view
    }

    companion object {
        fun newInstance(): DetailFragment =
            DetailFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createWebLink()
        if (sharedViewModel.searchForCharacters) {
            displayCharacter()
        } else {
            displaySerie()
        }
    }

    private fun createWebLink() {
        if (sharedViewModel.searchForCharacters) {
            if (sharedViewModel.clickedItem?.urls != null) {

                goto_button_detail.setOnClickListener {
                    var urlToCharacter: URLS =
                        Gson().fromJson(sharedViewModel.clickedItem?.urls!![0], URLS::class.java)
                    openWebLink(urlToCharacter)
                }
            }
        } else {
            if (sharedViewModel.clickedSeries?.urls != null) {
                goto_button_detail.setOnClickListener {
                    var urlToSeries: URLS =
                        Gson().fromJson(sharedViewModel.clickedSeries?.urls!![0], URLS::class.java)
                    openWebLink(urlToSeries)
                }
            }
        }
    }

    private fun openWebLink(url: URLS) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url.url)
        startActivity(openURL)
    }

    private fun displayCharacter() {
        val favoriteButton = view?.favorite_button
        val favouriteChecked = StitchCon.userData?.characters!!.find {
            val item = it as Document
            item["id"] == sharedViewModel.clickedItem?.id
        }

        favoriteButton?.isChecked = favouriteChecked != null

        var path = sharedViewModel.clickedItem?.thumbnail?.path
        path = path?.substring(0, 4) + "s" + path?.substring(4, path.length)
        val imageUrl =
            path + "/standard_medium." + sharedViewModel.clickedItem?.thumbnail?.extension

        Picasso.get().load(imageUrl)
            .error(R.drawable.cat)
            .into(details_image)
        details_name.text = sharedViewModel.clickedItem?.name
        details_description.text = sharedViewModel.clickedItem?.description
        Log.d("huehuehue", "${sharedViewModel.clickedItem}")

    }

    private fun displaySerie() {
        val favoriteButton = view?.favorite_button
        val favouriteChecked = StitchCon.userData?.series!!.find {
            val item = it as Document
            item["id"] == sharedViewModel.clickedSeries?.id
        }

        favoriteButton?.isChecked = favouriteChecked != null

        var path = sharedViewModel.clickedSeries?.thumbnail?.path
        path = path?.substring(0, 4) + "s" + path?.substring(4, path.length)
        val imageUrl =
            path + "/standard_medium." + sharedViewModel.clickedSeries?.thumbnail?.extension

        Picasso.get().load(imageUrl)
            .error(R.drawable.cat)
            .into(details_image)
        details_name.text = sharedViewModel.clickedSeries?.title
        details_description.text = sharedViewModel.clickedSeries?.description
    }
}