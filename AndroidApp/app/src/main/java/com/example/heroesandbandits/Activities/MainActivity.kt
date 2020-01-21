package com.example.heroesandbandits.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.util.Log.d
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.heroesandbandits.Fragments.FavoritesFragment
import com.example.heroesandbandits.Fragments.MessageFragment
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Fragments.SearchFragment
import com.example.heroesandbandits.Fragments.SearchNoResultFragment
import com.example.heroesandbandits.Models.Character
import com.example.heroesandbandits.Models.CharacterDataWrapper
import com.example.heroesandbandits.Utils.MarvelRetrofit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openFragment(SearchFragment.newInstance())
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


private val mOnNavigationItemSelectedListener =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_search -> {
                val searchFragment =
                    SearchFragment.newInstance()
                openFragment(searchFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_message -> {
                val messageFragment =
                    MessageFragment.newInstance()
                openFragment(messageFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                val favFragment =
                    FavoritesFragment.newInstance()
                openFragment(favFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


private fun openFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.container, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
}



}