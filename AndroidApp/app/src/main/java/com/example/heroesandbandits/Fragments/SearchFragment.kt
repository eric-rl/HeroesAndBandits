package com.example.heroesandbandits.Fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.Activities.SearchActivity
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.MarvelRetrofit
import com.example.heroesandbandits.Utils.StitchCon
import com.example.heroesandbandits.ViewModel.SharedViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.util.*

class SearchFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private val disposables = CompositeDisposable()
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel =
            activity?.let { ViewModelProviders.of(it).get(SharedViewModel::class.java) }!!
        val v = inflater.inflate(R.layout.fragment_search, container, false)

        if (searchHeroes) {
            v.heroesRadio.isChecked = true
            v.seriesRadio.isChecked = false
            if (sharedViewModel.searchResultsCharacter.isEmpty()) {
                replaceFragment(SearchDefaultFragment.newInstance())
            } else {
                replaceFragment(SearchResultFragment.newInstance())
            }
        } else {
            v.heroesRadio.isChecked = false
            v.seriesRadio.isChecked = true
            if (sharedViewModel.searchResultsSeries.isEmpty()) {
                replaceFragment(SearchDefaultFragment.newInstance())
            } else {
                replaceFragment(SeriesSearchResultFragment.newInstance())
            }
        }

        v.search_button.setOnClickListener {
            displaySearchResult()
            closeKeyboard()
        }

        v.radio_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = v.findViewById(checkedId)
            sharedViewModel.searchForHeroes = radio == v.heroesRadio
            searchHeroes = sharedViewModel.searchForHeroes

            /*FIX LOGIC WITH 'lastSearch' to avoid unnecessary fetch from api*/

            if (searchHeroes) {
//                if (sharedViewModel.searchResultsCharacter.isEmpty()) {
                    replaceFragment(SearchDefaultFragment.newInstance())
                    if(view!!.search_input.text.toString().trim().isNotEmpty()){
                        displaySearchResult()
                    }
//                } else {
//                    replaceFragment(SearchResultFragment.newInstance())
//                }
            } else {
//                if (sharedViewModel.searchResultsSeries.isEmpty()) {
                    replaceFragment(SearchDefaultFragment.newInstance())
                    if(view!!.search_input.text.toString().trim().isNotEmpty()){
                        displaySearchResult()
                    }
//                } else {
//                    replaceFragment(SeriesSearchResultFragment.newInstance())
//                }
            }
        }
        return v
    }

    fun closeKeyboard() {
        val activity = activity as SearchActivity

        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
    }

    private fun displaySearchResult() {
        val searchQuery =
            view!!.search_input.text.toString().trim().toLowerCase(Locale.getDefault())
        lastSearch = searchQuery
        val toBeDisposed: Disposable
        if (sharedViewModel.searchForHeroes) {
            toBeDisposed =
                MarvelRetrofit.marvelService.searchForCharacter(searchQuery)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe { result, err ->
                        if (err?.message != null) {
                            Log.d("___", "Something went wrong: ${err.message}")
                            replaceFragment(SearchNoResultFragment.newInstance())
                        } else {
                            if (result.data.results.isEmpty()) {
                                replaceFragment(SearchNoResultFragment.newInstance())
                            } else {
                                sharedViewModel.searchResultsCharacter.clear()
                                sharedViewModel.searchResultsCharacter.addAll(result.data.results)
                                replaceFragment(SearchResultFragment.newInstance())
                            }
                        }
                    }
        } else {
            toBeDisposed =
                MarvelRetrofit.marvelService.searchForSeries(searchQuery)
                    .subscribeOn(Schedulers.newThread()).subscribe { result, err ->
                        if (err?.message != null) {
                            Log.d("___", "Something went wrong: ${err.message}")
                            replaceFragment(SearchNoResultFragment.newInstance())
                        } else {
                            if (result.data.results.isEmpty()) {
                                replaceFragment(SearchNoResultFragment.newInstance())
                            } else {
                                d("___", "${result.data}")
                                sharedViewModel.searchResultsSeries.clear()
                                sharedViewModel.searchResultsSeries.addAll(result.data.results)
                                replaceFragment(SeriesSearchResultFragment.newInstance())
                            }

                        }
                    }
        }
        disposables.add(toBeDisposed)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        var lastSearch = ""
        var searchHeroes = true
        fun newInstance(): SearchFragment =
            SearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}

