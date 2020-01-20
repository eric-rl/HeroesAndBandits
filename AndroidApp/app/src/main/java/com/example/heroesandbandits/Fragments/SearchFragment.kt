package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.MarvelRetrofit
import com.example.heroesandbandits.ViewModel.SharedViewModel
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private var searchForHeroes: Boolean = true

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel =
            activity?.let { ViewModelProviders.of(it).get(SharedViewModel::class.java) }!!
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        replaceFragment(SearchDefaultFragment.newInstance())

        v.search_button.setOnClickListener {
            displaySearchResult()
        }


        v.radio_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = v.findViewById(checkedId)
            searchForHeroes = radio == v.heroesRadio
            Log.d("---", "$searchForHeroes")
        }
        return v
    }

    private fun displaySearchResult() {
        Log.d("---", view!!.search_input.text.toString())

        if (searchForHeroes) {

            MarvelRetrofit.marvelService.searchForCharacter(view!!.search_input.text.toString())
                .subscribeOn(Schedulers.newThread())
                .subscribe { result, err ->
                    if (err?.message != null) {
                        Log.d("___", "Something went wrong: ${err.message}")
                        replaceFragment(SearchNoResultFragment.newInstance())
                    } else {
                        if (result.data.results.isEmpty()) {
                            replaceFragment(SearchNoResultFragment.newInstance())
                        } else {
                            Log.d("'''", "$result")
                            sharedViewModel.searchResults.clear()
                            sharedViewModel.searchResults.addAll(result.data.results)
                            replaceFragment(SearchResultFragment.newInstance())
                        }

                    }
                }
        } else {
            MarvelRetrofit.marvelService.searchForSeries(view!!.search_input.text.toString())
                .subscribeOn(Schedulers.newThread()).subscribe { result, err ->
                    if (err?.message != null) {
                        Log.d("___", "Something went wrong: ${err.message}")
                        replaceFragment(SearchNoResultFragment.newInstance())
                    } else {
                        Log.d("___", "${result.data.results.get(0).title}")
                        if (result.data.results.isEmpty()) {
                            replaceFragment(SearchNoResultFragment.newInstance())
                        } else {
                            Log.d("'''", "$result")
                            replaceFragment(SearchResultFragment.newInstance())
                        }

                    }
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

