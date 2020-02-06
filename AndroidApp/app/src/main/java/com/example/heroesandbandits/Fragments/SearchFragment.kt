package com.example.heroesandbandits.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.Activities.SearchActivity
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.MarvelRetrofit
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
        v.search_button.setOnClickListener {
            val query = getQuery()
            if(query.isNotEmpty()){
                displaySearchResult(query)
            } else {
                replaceFragment(SearchDefaultFragment.newInstance())
            }
            closeKeyboard()
        }

        v.radio_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = v.findViewById(checkedId)
            sharedViewModel.searchForCharacters = radio == v.searchCharacterRadio
            searchCharacters = sharedViewModel.searchForCharacters
            val query = getQuery()
            when{
                query.isEmpty() -> replaceFragment(SearchDefaultFragment.newInstance())
                query == latestQuery -> displayData(query, v)
                else -> displaySearchResult(query)
            }
        }

        if(latestQuery.isEmpty()) {
            replaceFragment(SearchDefaultFragment.newInstance())
        } else {
            d("___","SEARH CREATE ELSE : " + v.searchCharacterRadio.isChecked + v.searchSeriesRadio.isChecked + searchCharacters)
            v.search_input.setText(latestQuery)
            displayData(latestQuery, v)
        }
        return v
    }
    fun displayData(query: String, v: View){
        when{
            sharedViewModel.searchForCharacters && query == latestCharacterQuery && sharedViewModel.searchResultsCharacter.isNotEmpty() -> {
                replaceFragment(SearchCharacterResultFragment.newInstance())
                v.searchCharacterRadio.isChecked = true
                v.searchSeriesRadio.isChecked = false
                d("___","DISPLAY 1")
            }
            !sharedViewModel.searchForCharacters && query == latestSeriesQuery && sharedViewModel.searchResultsSeries.isNotEmpty() -> {
                replaceFragment(SearchSeriesResultFragment.newInstance())
                v.searchCharacterRadio.isChecked = false
                v.searchSeriesRadio.isChecked = true
                d("___","DISPLAY 2 " + query  + " : " + latestSeriesQuery + ": "+  sharedViewModel.searchResultsSeries.size)
            }
            else -> displaySearchResult(query)
        }
    }

    private fun displaySearchResult(searchQuery:String) {
        d("___","DISPLAYSEARCHRESULT")

        latestQuery = searchQuery
        val toBeDisposed: Disposable
        if (sharedViewModel.searchForCharacters) {
            sharedViewModel.searchResultsCharacter.clear()
            latestCharacterQuery = latestQuery
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
                                sharedViewModel.searchResultsCharacter.addAll(result.data.results)
                                replaceFragment(SearchCharacterResultFragment.newInstance())
                            }
                        }
                    }
        } else {
            sharedViewModel.searchResultsSeries.clear()
            latestSeriesQuery = latestQuery
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
                                sharedViewModel.searchResultsSeries.addAll(result.data.results)
                                replaceFragment(SearchSeriesResultFragment.newInstance())
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

    fun closeKeyboard() {
        val activity = activity as SearchActivity

        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    companion object {
        var latestQuery = ""
        var latestCharacterQuery = ""
        var latestSeriesQuery = ""
        var searchCharacters = true
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

    fun getQuery():String{
         return if(view?.search_input != null)
             view!!.search_input.text.toString().trim().toLowerCase(Locale.getDefault())
         else ""
    }
}

