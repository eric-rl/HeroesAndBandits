package com.example.heroesandbandits.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.heroesandbandits.R
import com.example.heroesandbandits.ViewModel.SharedViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_favorites.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search.view.radio_group

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


        replaceFragment(FavoritesCharacterResultFragment.newInstance())
        v.radio_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = v.findViewById(checkedId)
            sharedViewModel.searchForCharacters = radio == v.favoriteCharacterRadio
            if(sharedViewModel.searchForCharacters){
                replaceFragment(FavoritesCharacterResultFragment.newInstance())
            } else{
                replaceFragment(FavoritesSeriesResultFragment.newInstance())
            }
        }
        return v
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
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}