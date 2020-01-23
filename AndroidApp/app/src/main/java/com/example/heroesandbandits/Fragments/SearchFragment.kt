package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.MarvelRetrofit
import com.example.heroesandbandits.ViewModel.SharedViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.view.*

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
        replaceFragment(SearchDefaultFragment.newInstance())

        v.search_button.setOnClickListener {
            displaySearchResult()
        }


        v.radio_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = v.findViewById(checkedId)
            sharedViewModel.searchForHeroes = radio == v.heroesRadio
        }
        return v
    }

    private fun displaySearchResult() {

        val toBeDisposed: Disposable

        if (sharedViewModel.searchForHeroes) {

            toBeDisposed =
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
                                sharedViewModel.searchResultsCharacter.clear()
                                sharedViewModel.searchResultsCharacter.addAll(result.data.results)
                                replaceFragment(SearchResultFragment.newInstance())
                            }

                        }
                    }
        } else {
            toBeDisposed =
                MarvelRetrofit.marvelService.searchForSeries(view!!.search_input.text.toString())
                    .subscribeOn(Schedulers.newThread()).subscribe { result, err ->
                        if (err?.message != null) {
                            Log.d("___", "Something went wrong: ${err.message}")
                            replaceFragment(SearchNoResultFragment.newInstance())
                        } else {
                            if (result.data.results.isEmpty()) {
                                replaceFragment(SearchNoResultFragment.newInstance())
                            } else {
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

