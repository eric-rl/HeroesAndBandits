package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.R
import com.example.heroesandbandits.ViewModel.SharedViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_search_character.view.*

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
        Log.d("what", "${sharedViewModel.clickedItem}")
        return view
    }

    companion object {
        fun newInstance(): DetailFragment =
            DetailFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("__", "Detail fragment säger HAÖÖÅÅSSÅÅÅ")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var path = sharedViewModel.clickedItem?.thumbnail?.path
        path = path?.substring(0, 4) + "s" + path?.substring(4, path.length)
        val imageUrl = path + "/standard_medium." + sharedViewModel.clickedItem?.thumbnail?.extension

        Picasso.get().load(imageUrl)
            .error(R.drawable.cat)
            .into(details_image)
        details_name.text = sharedViewModel.clickedItem?.name
    }
}