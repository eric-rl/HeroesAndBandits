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
import com.example.heroesandbandits.Utils.MarvelRetrofit
import io.reactivex.schedulers.Schedulers
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
        //   Log.d("---", v.search_input.text.toString())
          //  replaceFragment(SearchResultFragment.newInstance())

            displaySearchResult()
        }

        return v
    }

    private fun displaySearchResult(){
        Log.d("---", view!!.search_input.text.toString())

        MarvelRetrofit.marvelService.searchForCharacter(view!!.search_input.text.toString()).subscribeOn(Schedulers.newThread())
            .subscribe { result, err ->
                if (err?.message != null) Log.d("___", "No AntMan")

                else{
                    Log.d("___", "I got what i searched for ${result}")

                }

            }
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

