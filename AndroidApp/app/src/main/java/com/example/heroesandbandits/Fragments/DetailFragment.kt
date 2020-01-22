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
import com.example.heroesandbandits.ViewModel.SharedViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search_character.view.*
import org.json.JSONArray
import org.json.JSONObject

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

        if (sharedViewModel.clickedItem?.urls != null) {

                goto_button_detail.setOnClickListener {
                    var test : URLS = Gson().fromJson(sharedViewModel.clickedItem?.urls!![0], URLS::class.java)

                    Log.d("----", test.url)
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(test.url)
                    startActivity(openURL)

            }


            }


        var path = sharedViewModel.clickedItem?.thumbnail?.path
        path = path?.substring(0, 4) + "s" + path?.substring(4, path.length)
        val imageUrl =
            path + "/standard_medium." + sharedViewModel.clickedItem?.thumbnail?.extension

        Picasso.get().load(imageUrl)
            .error(R.drawable.cat)
            .into(details_image)
        details_name.text = sharedViewModel.clickedItem?.name
    }
}