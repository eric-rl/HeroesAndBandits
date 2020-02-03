package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.R
import com.example.heroesandbandits.ViewModel.SharedViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_search.view.*

class FavoritesFragment: Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private val disposables = CompositeDisposable()

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel =
            activity?.let { ViewModelProviders.of(it).get(SharedViewModel::class.java) }!!
        val v = inflater.inflate(R.layout.fragment_favorites, container, false)


        v.radio_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = v.findViewById(checkedId)
            sharedViewModel.searchForHeroes = radio == v.heroesRadio
        }
        return v
    }

    private fun displayResult(){
        val toBeDisposed: Disposable
        // if den ena knappen är tryckt visa

    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.favourite_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object{
        fun newInstance(): FavoritesFragment =
            FavoritesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        d("___", "Detta körs va?")
        replaceFragment(FavoritesResultFragment.newInstance())
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}